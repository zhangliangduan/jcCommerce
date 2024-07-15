package com.xunmeng.jccommerce.Logic;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xunmeng.jccommerce.Converter.CommonConverter;
import com.xunmeng.jccommerce.base.BusinessException;
import com.xunmeng.jccommerce.base.Result;
import com.xunmeng.jccommerce.domain.JcOrders;
import com.xunmeng.jccommerce.domain.JcOrdersOperationEdit;
import com.xunmeng.jccommerce.dto.count.SalesDifferenceVO;
import com.xunmeng.jccommerce.dto.order.JcOrdersOperationEditVO;
import com.xunmeng.jccommerce.dto.order.JcOrdersVO;
import com.xunmeng.jccommerce.dto.order.ListQo;
import com.xunmeng.jccommerce.dto.page.PageQo;
import com.xunmeng.jccommerce.dto.page.PageVo;
import com.xunmeng.jccommerce.enums.ErrorCodeEnum;
import com.xunmeng.jccommerce.service.IJcOrdersOperationEditService;
import com.xunmeng.jccommerce.service.IJcOrdersService;
import com.xunmeng.jccommerce.util.AuthUtil;
import com.xunmeng.jccommerce.util.PageVoUtil;
import com.xunmeng.jccommerce.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Log4j2
public class OrderLogic {

    private final IJcOrdersService jcOrdersService;
    private final CommonConverter commonConverter;
    private final IJcOrdersOperationEditService jcOrdersOperationEditService;

    @Value("${default.project.web.path}")
    private String webPath;

    /**
     * 订单列表
     *
     * @param req 列表公共请求参数
     * @return 分页数据
     */
    public PageVo<JcOrdersVO> list(ListQo req) {
        Page<JcOrders> page = new Page<>(req.getCurrent(), req.getSize());
        LambdaQueryChainWrapper<JcOrders> wrapper = jcOrdersService.lambdaQuery()
                .eq(Objects.equals(req.getIsAfterSale(), 1), JcOrders::getIsAfterSale, 1)
                .orderByDesc(JcOrders::getCreateTime);

        if (Objects.nonNull(req.getStart())) {
            wrapper.ge(JcOrders::getCreateTime, req.getStart().getTime() / 1000);
        }
        if (Objects.nonNull((req.getEnd()))) {
            wrapper.le(JcOrders::getCreateTime, req.getEnd().getTime() / 1000);
        }
        if (StrUtil.isNotBlank(req.getId())) {
            wrapper.eq(JcOrders::getId, req.getId().trim());
        }
        if (StrUtil.isNotBlank(req.getName())) {
            wrapper.eq(JcOrders::getName, req.getName().trim());
        }
        if (StrUtil.isNotBlank(req.getPhone())) {
            wrapper.eq(JcOrders::getPhone, req.getPhone().trim());
        }
        if (StrUtil.isNotBlank(req.getHolder())) {
            wrapper.eq(JcOrders::getHolder, req.getHolder().trim());
        }

        Page<JcOrders> pageResult = wrapper.page(page);
        if (CollUtil.isEmpty(pageResult.getRecords())) {
            return new PageVo<>();
        }

        PageVo<JcOrdersVO> voList = PageVoUtil.getPageVo(pageResult);
        List<JcOrders> records = pageResult.getRecords();
        List<JcOrdersVO> recordsVO = records.stream().map(commonConverter::JcOrdersToJcOrdersVO).collect(Collectors.toList());
        for (JcOrdersVO jcOrdersVO : recordsVO) {
            Long createTime = jcOrdersService.getById(jcOrdersVO.getId()).getCreateTime();
            jcOrdersVO.setCreateTime(createTime);
        }
        Set<String> phones = records.stream().map(JcOrders::getPhone).collect(Collectors.toSet());
        Map<String, Long> phonesPurchaseMap = jcOrdersService.getPhonesPurchaseMap(phones);
        recordsVO.forEach(o -> {
            Long purchasesNumber = phonesPurchaseMap.get(o.getPhone());
            o.setPurchasesNumber(purchasesNumber);
        });
        voList.setRecords(recordsVO);
        return voList;
    }


    /**
     * 从Excel文件导入订单信息。
     *
     * @param file 上传的Excel文件
     * @throws BusinessException 如果导入过程中出现错误或数据问题
     */
    public void inputExcel(MultipartFile file) {
        log.info("导入EXCEL");
        List<JcOrders> orders;
        try (InputStream inputStream = file.getInputStream()) {
            orders = EasyExcel.read(inputStream)
                    .head(JcOrders.class)
                    .sheet()
                    .doReadSync();
            if (CollUtil.isEmpty(orders)) {
                throw new BusinessException(ErrorCodeEnum.FAIL, "没有可以导入的记录");
            }


            // 检查导入的orders中是否有重复的id
            Set<String> seenIds = new HashSet<>();
            Set<String> duplicateIds = new HashSet<>();
            for (JcOrders order : orders) {
                if (!seenIds.add(order.getId())) {
                    duplicateIds.add(order.getId());
                }
            }

            if (!duplicateIds.isEmpty()) {
                throw new BusinessException(ErrorCodeEnum.FAIL, "重复的订单ID：" + String.join(", ", duplicateIds));
            }

            List<String> existingIds = jcOrdersService.getExistingOrderIds();
            List<JcOrders> newOrders = orders.stream()
                    .filter(order -> !existingIds.contains(order.getId()))
                    .collect(Collectors.toList());
            if (CollUtil.isEmpty(newOrders)) {
                throw new BusinessException(ErrorCodeEnum.FAIL, "所有记录已全部导入");
            }
            jcOrdersService.saveBatch(newOrders);
            String userName = AuthUtil.getCurrentUserInfo().getUserName();

            String fileName = saveFileToLocal(file);
            String url = webPath + fileName;
            JcOrdersOperationEdit newEdit = new JcOrdersOperationEdit();
            newEdit.setName(userName);
            newEdit.setOperationType("订单导入");
            newEdit.setUrl(url);
            jcOrdersOperationEditService.save(newEdit);

        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(ErrorCodeEnum.FAIL, e.getMessage());
        }
    }

    /**
     * 将上传的文件保存到本地，并更新订单操作编辑信息。
     *
     * @param file 上传的文件对象。
     */
    private String saveFileToLocal(MultipartFile file) {
        try {
            // 创建文件夹如果不存在
            Path uploadPath = Paths.get("/home/api/jcCommerce/excel");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 保存文件到指定目录
            String fileName = System.currentTimeMillis() / 1000 + ".xlsx";
            Path filePath = uploadPath.resolve(fileName);
            file.transferTo(filePath.toFile());
            return fileName;
        } catch (IOException e) {
            log.error("文件保存失败: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCodeEnum.FAIL, "文件保存失败: " + e.getMessage());
        }
    }

    /**
     * 订单售后。
     *
     * @param ids             订单ID。
     * @param afterSaleReason 售后原因。
     */
    public void afterSale(List<String> ids, String afterSaleReason, BigDecimal amount) {
        List<JcOrders> orders = jcOrdersService.listByIds(ids);
        if (CollUtil.isEmpty(orders)) {
            throw new BusinessException(ErrorCodeEnum.FAIL, ids + "订单不存在");
        }

        List<String> alreadyAfterSaleIds = orders.stream()
                .filter(order -> order.getIsAfterSale() == 1)
                .map(JcOrders::getId)
                .collect(Collectors.toList());
        if (!alreadyAfterSaleIds.isEmpty()) {
            throw new BusinessException(ErrorCodeEnum.FAIL, "订单已售后: " + String.join(", ", alreadyAfterSaleIds));
        }

        if (StrUtil.isEmpty(afterSaleReason)) {
            throw new BusinessException(ErrorCodeEnum.FAIL, "售后原因不能为空");
        }

        List<String> insufficientAmountIds = orders.stream()
                .filter(order -> order.getSale().subtract(amount).compareTo(BigDecimal.ZERO) < 0)
                .map(JcOrders::getId)
                .collect(Collectors.toList());
        if (!insufficientAmountIds.isEmpty()) {
            throw new BusinessException(ErrorCodeEnum.FAIL, "扣减金额不能大于销售金额的订单: " + String.join(", ", insufficientAmountIds));
        }

        // 构建批量更新条件
        List<JcOrders> updatedOrders = orders.stream()
                .peek(order -> {
                    order.setIsAfterSale(1);
                    order.setAfterSaleReason(afterSaleReason);
                    order.setSale(order.getSale().subtract(amount));
                    order.setAfterSaleAmount(amount);
                })
                .collect(Collectors.toList());

        boolean update = jcOrdersService.updateBatchById(updatedOrders);
        if (!update) {
            throw new BusinessException(ErrorCodeEnum.FAIL, "批量更新失败");
        }
        String userName = AuthUtil.getCurrentUserInfo().getUserName();
        Boolean recordResult = jcOrdersOperationEditService.saveRecord(userName, "售后");
        if (!recordResult) {
            throw new BusinessException(ErrorCodeEnum.FAIL, "记录操作失败");
        }
    }

    /**
     * 计算销售趋势的方法。
     * 该方法用于根据指定的开始日期和结束日期，查询这段时间内的订单销售情况，以每天的销售总额为单位进行统计。
     *
     * @param startDate 开始日期，用于限定查询订单的时间范围。
     * @param endDate   结束日期，用于限定查询订单的时间范围。
     * @return 返回一个包含每天销售总额的结果对象。
     */
    public Result<Map<LocalDate, Map<String, BigDecimal>>> saleTrend(Date startDate, Date endDate) {
        Map<LocalDate, Map<String, BigDecimal>> dailyStats = new LinkedHashMap<>();

        LocalDate localStartDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localEndDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        for (LocalDate date = localStartDate; !date.isAfter(localEndDate); date = date.plusDays(1)) {
            dailyStats.put(date, new HashMap<>());
            dailyStats.get(date).put("sales", BigDecimal.ZERO);
            dailyStats.get(date).put("cost", BigDecimal.ZERO);
            dailyStats.get(date).put("income", BigDecimal.ZERO);
        }

        // 查询销售数据
        List<JcOrders> salesList = jcOrdersService.lambdaQuery()
                .select(JcOrders::getCreateTime, JcOrders::getSale)
                .eq(JcOrders::getIsAfterSale, 0)
                .ge(JcOrders::getCreateTime, (startDate.getTime() / 1000) + 86400)
                .le(JcOrders::getCreateTime, (endDate.getTime() / 1000) + 86400)
                .list();

        // 查询成本数据
        List<JcOrders> costsList = jcOrdersService.lambdaQuery()
                .select(JcOrders::getCreateTime, JcOrders::getCost)
                .eq(JcOrders::getIsAfterSale, 0)
                .ge(JcOrders::getCreateTime, (startDate.getTime() / 1000) + 86400)
                .le(JcOrders::getCreateTime, (endDate.getTime() / 1000) + 86400)
                .list();

        // 汇总销售数据
        Map<LocalDate, BigDecimal> actualSales = salesList.stream()
                .collect(Collectors.groupingBy(
                        order -> Instant.ofEpochSecond(order.getCreateTime()).atZone(ZoneId.systemDefault()).toLocalDate(),
                        Collectors.reducing(BigDecimal.ZERO, JcOrders::getSale, BigDecimal::add)
                ));

        // 汇总成本数据
        Map<LocalDate, BigDecimal> actualCosts = costsList.stream()
                .collect(Collectors.groupingBy(
                        order -> Instant.ofEpochSecond(order.getCreateTime()).atZone(ZoneId.systemDefault()).toLocalDate(),
                        Collectors.reducing(BigDecimal.ZERO, JcOrders::getCost, BigDecimal::add)
                ));

        // 更新dailyStats中的销售和成本数据
        dailyStats.forEach((date, stats) -> {
            stats.put("sales", stats.get("sales").add(actualSales.getOrDefault(date, BigDecimal.ZERO)));
            stats.put("cost", stats.get("cost").add(actualCosts.getOrDefault(date, BigDecimal.ZERO)));
            stats.put("income", stats.get("sales").subtract(stats.get("cost")));
        });

        return Result.newSuccessResponse(dailyStats);
    }

    /**
     * 计算售后趋势的结果。
     * 该方法通过查询指定日期范围内的售后订单金额，来分析售后趋势。
     *
     * @param startDate 查询的开始日期。
     * @param endDate   查询的结束日期。
     * @return 返回售后趋势的结果，以日期为键，售后金额为值的映射。
     */
    public Result<Map<LocalDate, BigDecimal>> afterSaleTrend(Date startDate, Date endDate) {

        Map<LocalDate, BigDecimal> dailySales = new LinkedHashMap<>();

        LocalDate localStartDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localEndDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        for (LocalDate date = localStartDate; !date.isAfter(localEndDate); date = date.plusDays(1)) {
            dailySales.put(date, BigDecimal.ZERO);
        }

        List<JcOrders> afterSaleList = jcOrdersService.lambdaQuery()
                .select(JcOrders::getCreateTime, JcOrders::getAfterSaleAmount)
                .eq(JcOrders::getIsAfterSale, 1)
                .ge(JcOrders::getCreateTime, startDate.getTime() / 1000 + 86400) // 注意这里除以1000
                .le(JcOrders::getCreateTime, endDate.getTime() / 1000 + 86400)   // 注意这里除以1000
                .list();

        // Use stream to collect the actual after sales data
        Map<LocalDate, BigDecimal> actualAfterSales = afterSaleList.stream()
                .collect(Collectors.groupingBy(
                        order -> Instant.ofEpochSecond(order.getCreateTime()).atZone(ZoneId.systemDefault()).toLocalDate(),
                        Collectors.reducing(BigDecimal.ZERO, JcOrders::getAfterSaleAmount, BigDecimal::add)
                ));

        dailySales.putAll(actualAfterSales);

        return Result.newSuccessResponse(dailySales);
    }


    /**
     * 订单取消售后。
     *
     * @param id 订单ID。
     */
    public void cancelAfterSale(String id) {
        JcOrders byId = jcOrdersService.getById(id);
        if (byId.getIsAfterSale() == 0) {
            throw new BusinessException(ErrorCodeEnum.FAIL, id + "订单未售后");
        }
        LambdaUpdateChainWrapper<JcOrders> wrapper = jcOrdersService.lambdaUpdate()
                .eq(JcOrders::getId, id)
                .set(JcOrders::getIsAfterSale, 0)
                .set(JcOrders::getAfterSaleAmount, 0)
                .set(JcOrders::getSale, byId.getAfterSaleAmount().add(byId.getSale()))
                .set(JcOrders::getAfterSaleReason, null);
        boolean update = wrapper.update();
        if (!update) {
            throw new BusinessException(ErrorCodeEnum.FAIL, "订单不存在");
        }
        String userName = AuthUtil.getCurrentUserInfo().getUserName();
        Boolean recordResult = jcOrdersOperationEditService.saveRecord(userName, "售后");
        if (!recordResult) {
            throw new BusinessException(ErrorCodeEnum.FAIL, "记录操作失败");
        }

    }

    /**
     * 操作导出。
     *
     * @return 订单数据。
     */

    public PageVo<JcOrdersOperationEditVO> getEditPage(PageQo req) {
        Page<JcOrdersOperationEdit> page = new Page<>(req.getCurrent(), req.getSize());
        LambdaQueryChainWrapper<JcOrdersOperationEdit> wrapper = jcOrdersOperationEditService.lambdaQuery()
                .select(JcOrdersOperationEdit::getName, JcOrdersOperationEdit::getCreateTime, JcOrdersOperationEdit::getOperationType, JcOrdersOperationEdit::getUrl)
                .orderByDesc(JcOrdersOperationEdit::getCreateTime);
        Page<JcOrdersOperationEdit> editPage = wrapper.page(page);
        if (CollUtil.isEmpty(editPage.getRecords())) {
            return new PageVo<>();
        }
        PageVo<JcOrdersOperationEditVO> voList = PageVoUtil.getPageVo(editPage);
        List<JcOrdersOperationEdit> records = editPage.getRecords();
        List<JcOrdersOperationEditVO> recordsVO = records.stream().map(commonConverter::JcOrdersOperationEditToJcOrdersOperationEditVO).collect(Collectors.toList());
        voList.setRecords(recordsVO);
        return voList;
    }

    /**
     * 删除订单。
     *
     * @param id 订单ID。
     * @return 删除结果。
     */
    public Result<String> deleteOrder(String id) {
        boolean isDeleted = jcOrdersService.removeById(id);
        String result = isDeleted ? "删除成功" : "删除失败";
        if (isDeleted) {
            String userName = AuthUtil.getCurrentUserInfo().getUserName();
            Boolean recordResult = jcOrdersOperationEditService.saveRecord(userName, "删除订单");
            if (!recordResult) {
                throw new BusinessException(ErrorCodeEnum.FAIL, "记录操作失败");
            }
        }
        return Result.newSuccessResponse(result);
    }

   /**
     * 获取商品销售周期差值。
     *
     * @param day 周期天数。
     * @return 商品销售周期差值。
     */
    public Result<SalesDifferenceVO> saleTrendWithDifference(Integer day) {
        SalesDifferenceVO trendDifferences = new SalesDifferenceVO();

        LocalDate localEndDate = LocalDate.from(Objects.requireNonNull(TimeUtil.getLocalDateTime(TimeUtil.getTodayZeroTimestamp()+86400)));
        LocalDate localStartDate = localEndDate.plusDays(-day);

        long value = TimeUtil.timeGapDays(localStartDate.atStartOfDay(), localEndDate.atStartOfDay());

        LocalDate prevStart = localStartDate.minusDays(value);
        LocalDate prevEnd = localEndDate.minusDays(value);

        Map<String, BigDecimal> currentData = queryDataForPeriod(localStartDate, localEndDate);

        Map<String, BigDecimal> previousData = queryDataForPeriod(prevStart, prevEnd);

        trendDifferences.setSaleDifference(calculateDifference(currentData.get("sales"), previousData.get("sales")));
        trendDifferences.setCostDifference(calculateDifference(currentData.get("cost"), previousData.get("cost")));
        trendDifferences.setInComeDifference(calculateDifference(currentData.get("income"), previousData.get("income")));
        trendDifferences.setCountDifference(calculateDifference(currentData.get("quantity").intValue(), previousData.get("quantity").intValue()));
        trendDifferences.setAfterSaleDifference(calculateDifference(currentData.get("afterSaleAmount"), previousData.get("afterSaleAmount")));
        trendDifferences.setAfterSaleCountDifference(calculateDifference(currentData.get("afterSaleQuantity").intValue(), previousData.get("afterSaleQuantity").intValue()));
        return Result.newSuccessResponse(trendDifferences);

    }

    /**
     * 计算两个BigDecimal类型数值的差值。
     * <p>
     * 此方法用于精确地计算两个大数值的差。使用BigDecimal类可以避免浮点数计算带来的精度问题。
     * 如果任何一个输入值为null，则返回0，以避免空指针异常。
     *
     * @param current 当前值，用于计算差值的BigDecimal对象。
     * @param previous 前一个值，用于计算差值的BigDecimal对象。
     * @return 返回当前值与前一个值的差值，如果任一输入值为null，则返回0。
     */
    private BigDecimal calculateDifference(BigDecimal current, BigDecimal previous) {
        if (current == null || previous == null) {
            return BigDecimal.ZERO;
        }
        return current.subtract(previous);
    }

    /**
     * 计算两个整数类型的数值的差值。
     * <p>
     * 此方法用于精确地计算两个整数类型的数值的差。
     * 如果任何一个输入值为null，则返回0，以避免空指针异常。
     *
     * @param current 当前值，用于计算差值的整数类型。
     * @param previous 前一个值，用于计算差值的整数类型。
     * @return 返回当前值与前一个值的差值，如果任一输入值为null，则返回0。
     */
    private Integer calculateDifference(int current, int previous) {
        return  current - previous;
    }

    /**
     * 根据给定的日期范围，查询指定时间段内的销售数据。
     *
     * @param start 起始日期，用于定义统计周期的起始点。
     * @param end 结束日期，用于定义统计周期的结束点。
     * @return 返回一个包含销售数据的Map对象，其中键为数据类型（如"sales"、"cost"等），值为对应类型的销售数据。
     */
    private Map<String, BigDecimal> queryDataForPeriod(LocalDate start, LocalDate end) {
        long startTime = start.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000;
        long endTime = end.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000;

        Map<String, BigDecimal> data = new HashMap<>();

        LambdaQueryWrapper<JcOrders> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(JcOrders::getCreateTime, startTime);
        wrapper.le(JcOrders::getCreateTime, endTime);
        wrapper.eq(JcOrders::getIsAfterSale, 0);

        LambdaQueryWrapper<JcOrders> afterSaleWrapper = new LambdaQueryWrapper<>();
        afterSaleWrapper.ge(JcOrders::getCreateTime, startTime);
        afterSaleWrapper.le(JcOrders::getCreateTime, endTime);
        afterSaleWrapper.eq(JcOrders::getIsAfterSale, 1);

        BigDecimal sales = jcOrdersService.list(wrapper).stream()
                .map(JcOrders::getSale)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        data.put("sales", sales);

        BigDecimal cost = calculateCostForPeriod(startTime, endTime);
        data.put("cost", cost);

        BigDecimal income = sales.subtract(cost);
        data.put("income", income);

        int quantity = jcOrdersService.list(wrapper).size();
        data.put("quantity", new BigDecimal(quantity));

        BigDecimal afterSaleSales = jcOrdersService.list(afterSaleWrapper).stream()
                .map(JcOrders::getAfterSaleAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        data.put("afterSaleAmount", afterSaleSales);

        int afterSaleQuantity = jcOrdersService.list(afterSaleWrapper).size();
        data.put("afterSaleQuantity", new BigDecimal(afterSaleQuantity));

        return data;
    }


    /**
     * 计算指定时间段内的成本。
     * <p>
     * 此方法用于计算指定时间段内的订单成本。
     *
     * @param startTime 起始时间戳，以秒为单位。
     * @param endTime   结束时间戳，以秒为单位。
     * @return 返回指定时间段内的订单成本。
     */
    private BigDecimal calculateCostForPeriod(long startTime, long endTime) {
        LambdaQueryWrapper<JcOrders> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(JcOrders::getCreateTime, startTime);
        wrapper.le(JcOrders::getCreateTime, endTime);

        List<JcOrders> orders = jcOrdersService.list(wrapper);
        return orders.stream()
                .map(JcOrders::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}