package com.ruoyi.advertisement.service;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.WebColors;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.config.ServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.itextpdf.layout.property.VerticalAlignment;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class PdfService {
    // 添加字体缓存
    private PdfFont cachedFont = null;
    private boolean fontLoadAttempted = false;
    //添加预加载字体
    private PdfFont loadFont() {
        if (cachedFont != null) {
            return cachedFont;
        }

        if (fontLoadAttempted) {
            return null; // 避免重复尝试加载失败的字体
        }

        fontLoadAttempted = true;
        try {
            cachedFont = PdfFontFactory.createFont("fonts/MiSans.ttf", "Identity-H", true);
            return cachedFont;
        } catch (Exception e) {
            System.err.println("字体加载失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 自定义MultipartFile实现类，用于包装字节数组为MultipartFile对象
     * 实现了Spring的MultipartFile接口，提供文件上传所需的基本功能
     */
    private static class CustomMultipartFile implements MultipartFile {
        private final byte[] content;
        private final String name;
        private final String originalFilename;
        private final String contentType;

        /**
         * 构造函数，创建CustomMultipartFile实例
         * @param content 文件内容字节数组
         * @param name 文件参数名称
         * @param originalFilename 原始文件名
         * @param contentType 文件内容类型
         */
        public CustomMultipartFile(byte[] content, String name, String originalFilename, String contentType) {
            this.content = content;
            this.name = name;
            this.originalFilename = originalFilename;
            this.contentType = contentType;
        }

        /**
         * 获取文件参数名称
         * @return 文件参数名称
         */
        @Override
        public String getName() {
            return name;
        }

        /**
         * 获取原始文件名
         * @return 原始文件名
         */
        @Override
        public String getOriginalFilename() {
            return originalFilename;
        }

        /**
         * 获取文件内容类型
         * @return 文件内容类型
         */
        @Override
        public String getContentType() {
            return contentType;
        }

        /**
         * 判断文件是否为空
         * @return 当内容为null或长度为0时返回true，否则返回false
         */
        @Override
        public boolean isEmpty() {
            return content == null || content.length == 0;
        }

        /**
         * 获取文件大小
         * @return 文件字节长度，如果内容为null则返回0
         */
        @Override
        public long getSize() {
            return content != null ? content.length : 0;
        }

        /**
         * 获取文件字节数组
         * @return 文件内容字节数组
         */
        @Override
        public byte[] getBytes()  {
            return content;
        }

        /**
         * 获取文件输入流
         * @return 基于文件内容的ByteArrayInputStream对象
         */
        @Override
        public InputStream getInputStream()  {
            return new ByteArrayInputStream(content);
        }

        /**
         * 将文件内容传输到指定文件
         * @param dest 目标文件
         * @throws FileNotFoundException 当目标文件无法创建或访问时抛出
         */
        @Override
        public void transferTo(File dest) throws FileNotFoundException {
            // 使用FileOutputStream将文件内容写入目标文件
            try (FileOutputStream fos = new FileOutputStream(dest)) {
                fos.write(content);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Autowired
    private ServerConfig serverConfig;

    /**
     * 生成PDF文档
     * @param data 包含文档数据的Map对象，用于填充PDF内容
     * @return 返回生成的PDF文档字节数组
     */
    public byte[] generatePdf(Map<String, Object> data) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // 创建 PDF 文档
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);

        // 加载中文字体
        PdfFont font = null;
        try {
            font = loadFont();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 设置文档标题和编号信息
        Paragraph title = new Paragraph("户外广告监测分析系统")
                .setFont(font) // 设置字体
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(16);
        doc.add(title);
        Paragraph testNumber = new Paragraph("单据编号："+getStringValue(data,"adCode"))
                .setFont(font) // 设置字体
                .setTextAlignment(TextAlignment.RIGHT); // 右对齐
        doc.add(testNumber);

        // 创建表格并指定列宽
        float[] columnWidths = {2.0f, 3.0f, 2.0f, 3.0f}; // 调整列宽比例
        Table table = new Table(columnWidths);
        table.setWidth(UnitValue.createPercentValue(100));

        // 填充数据
        fillTableData(table, data, font); // 传递字体参数

        // 添加图片（如果有）
        addImageToDocument(table, data, font);

        // 将表格添加到文档
        doc.add(table);
        // 添加水印
        addWatermark(pdfDoc, font);
        // 关闭文档
        doc.close();

        return baos.toByteArray();
    }

    // 添加水印方法
    private void addWatermark(PdfDocument pdfDoc, PdfFont font) {
        if (font == null) {
            return; // 字体不可用时不添加水印
        }
        // 获取页面数量
        int numberOfPages = pdfDoc.getNumberOfPages();

        // 水印文本
        Paragraph watermark = new Paragraph("广告监测")
                .setFont(font)
                .setFontSize(40)
                .setFontColor(ColorConstants.LIGHT_GRAY);

        // 设置水印透明度
        watermark.setProperty(Property.OPACITY, 0.3f); // 透明度设置为 20%

        // 水印间隔距离（单位：用户空间单位，1单位 ≈ 0.75像素）
        float watermarkInterval = 200;

        // 为每一页添加水印
        for (int i = 1; i <= numberOfPages; i++) {
            // 获取页面尺寸
            Rectangle pageSize = pdfDoc.getPage(i).getPageSize();

            // 创建 PdfCanvas 对象
            PdfCanvas canvas = new PdfCanvas(pdfDoc.getPage(i));

            // 保存当前状态
            canvas.saveState();

            // 计算水印的起始位置和结束位置
            float startX = watermarkInterval;
            float endX = pageSize.getWidth() - watermarkInterval;
            float startY = watermarkInterval;
            float endY = pageSize.getHeight() - watermarkInterval;

            // 在每个指定位置添加水印
            for (float x = startX; x < endX; x += watermarkInterval) {
                for (float y = startY; y < endY; y += watermarkInterval) {
                    // 添加水印（旋转45度）
                    new Canvas(canvas, pageSize)
                            .showTextAligned(watermark, x, y, i, TextAlignment.CENTER, VerticalAlignment.MIDDLE, 45);
                    // 在右侧相同位置添加水印
                    new Canvas(canvas, pageSize)
                            .showTextAligned(watermark, x+300, y, i, TextAlignment.CENTER, VerticalAlignment.MIDDLE, 45);
                }
            }

            // 恢复状态
            canvas.restoreState();
        }
    }


    private void fillTableData(Table table, Map<String, Object> data, PdfFont font) {
        String backgroundColor = "#E0E0E0";

        // 基本信息
        addRowWithBg(table, "营利类型", getProfitabilityTypeName(getStringValue(data, "adProfitabilityType")),
                "行业分类", getStringValue(data, "adIndustryType"),
                font, backgroundColor);

        addRowWithBg(table,"媒体类型", getStringValue(data, "adMediumType") ,
                "违规类别", getViolationType(getStringValue(data, "violationType")) , font, backgroundColor);

        addRowWithBg(table, "广告描述", getStringValue(data, "adDescription"),
                "广告主", getStringValue(data, "advertiser"), font, backgroundColor);

        addRowWithBg(table, "采集人", getStringValue(data, "surveyor"),
                "采集时间", formatDateTime(getStringValue(data, "surveyTime")), font, backgroundColor);

        // 时间信息
        addRowWithBg(table, "行政区域",getRegion(data),
                "详细地址", getStringValue(data, "address"), font, backgroundColor);

        // 地址信息
        addRowWithBg(table, "备注", getStringValue(data, "remark"),
                "处理人", getStringValue(data, "handler"), font, backgroundColor);

        addRowWithBg(table, "处理时间", formatDateTime(getStringValue(data, "handleTime")),
                "处理结果", getHandleResult(getStringValue(data, "handleResult")), font, backgroundColor);

    }
    private void addRowWithBg(Table table, String field1, String value1, String field2, String value2, PdfFont font, String backgroundColor) {
        table.addCell(createCellWithBg(field1, font, backgroundColor, TextAlignment.CENTER))
                .addCell(createCell(value1, font, TextAlignment.CENTER))
                .addCell(createCellWithBg(field2, font, backgroundColor, TextAlignment.CENTER))
                .addCell(createCell(value2, font, TextAlignment.CENTER));
    }


    private Cell createCellWithBg(String content, PdfFont font, String backgroundColor, TextAlignment alignment) {
        return new Cell()
                .add(new Paragraph(content != null ? content : "-").setFont(font))
                .setBackgroundColor(WebColors.getRGBColor(backgroundColor))
                .setTextAlignment(alignment);
    }

    private Cell createCell(String content, PdfFont font, TextAlignment alignment) {
        return new Cell()
                .add(new Paragraph(content != null ? content : "-").setFont(font))
                .setTextAlignment(alignment);
    }

    /**
     * 从Map中获取指定键的字符串值
     *
     * @param data 包含键值对的Map对象
     * @param key 要获取值的键
     * @return 如果键存在且值不为null，则返回值的字符串表示；否则返回null
     */
    private String getStringValue(Map<String, Object> data, String key) {
        Object value = data.get(key);
        return value != null ? value.toString() : null;
    }
    /**
     * 根据代码获取盈利类型名称
     * @param code 盈利类型代码，"1"表示商业广告，"2"表示公益广告
     * @return 返回对应的盈利类型名称，如果代码为null则返回"-"，如果代码不匹配则返回"未知"
     */
    private String getProfitabilityTypeName(String code) {
        if (code == null) return "-";
        switch (code) {
            case "1": return "商业广告";
            case "2": return "公益广告";
            default: return "未知";
        }
    }


    /**
     * 根据违规类型代码获取对应的违规类型名称
     * @param code 违规类型代码，可能的值包括：
     *             "1"-禁用词类, "2"-虚假宣传, "3"-低俗类, "4"-敏感类, "5"-其他类
     * @return 对应的违规类型名称，如果代码不匹配则返回"未知"，如果代码为null则返回"-"
     */
    private String getViolationType(String code) {
        if (code == null) return "-";
        switch (code) {
            case "1": return "禁用词类";
            case "2": return "虚假宣传";
            case "3": return "低俗类";
            case "4": return "敏感类";
            case "5": return "其他类";
            default: return "未知";
        }
    }

    /**
     * 根据处理结果代码获取对应的中文描述
     * @param code 处理结果代码，null表示无状态
     * @return 返回处理结果的中文描述：已拆除处理、已罚款处理或未知
     */
    private String getHandleResult(String code) {
        if (code == null) return "-";
        switch (code) {
            case "1": return "已拆除处理";
            case "2": return "已罚款处理";
            default: return "未知";
        }
    }
    /**
     * 从数据映射中获取地区信息，按省、市、区、街道的顺序拼接成完整地区字符串
     * @param data 包含地区信息的键值对映射，key为地区级别，value为地区名称
     * @return 拼接后的完整地区字符串，各地区级别之间无分隔符
     */
    private String getRegion(Map<String, Object> data){
        StringBuilder result = new StringBuilder();
        // 依次拼接省、市、区、街道信息
        result.append(getValueOrDefault(data, "province"));
        result.append(getValueOrDefault(data, "city"));
        result.append(getValueOrDefault(data, "district"));
        result.append(getValueOrDefault(data, "street"));
        return result.toString();
    }

    /**
     * 从数据映射中获取指定键的值，如果值不存在则返回默认值"-"
     * @param data 数据映射
     * @param key 要获取值的键名
     * @return 键对应的值（转换为字符串）或默认值"-"
     */
    private String getValueOrDefault(Map<String, Object> data, String key) {
        Object value = data.get(key);
        return value != null ? value.toString() : "-";
    }

    private void addImageToDocument(Table table, Map<String, Object> data, PdfFont font) {
        // 处理初审图片和执行图片
        processAndAddImages(table, data, font, "adImages", "执行前图片");
        processAndAddImages(table, data, font, "postHandleImage", "执行后图片");
    }

    /**
     * 处理图片数据并添加到PDF表格中
     *
     * @param table PDF表格对象，用于添加图片和文本单元格
     * @param data 数据映射，包含图片路径等信息
     * @param font PDF字体，用于设置文本样式
     * @param imageKey 图片路径在数据映射中的键名
     * @param fieldName 字段名称，用作图片区域的标题
     */
    private void processAndAddImages(Table table, Map<String, Object> data, PdfFont font, String imageKey, String fieldName) {
        String imagePaths = getStringValue(data, imageKey);

        if (imagePaths == null || imagePaths.isEmpty()) {
            return;
        }

        try {
            // 创建标题单元格
            Cell textCell = new Cell(1, 1)
                    .add(new Paragraph(fieldName)
                            .setFont(font)
                            .setBold()
                            .setTextAlignment(TextAlignment.CENTER))
                    .setBackgroundColor(WebColors.getRGBColor("#E0E0E0"));
            table.addCell(textCell);

            // 创建图片单元格
            Cell imageCell = new Cell(1, 3);
            Div div = new Div();

            // 处理每个图片
            String[] paths = imagePaths.split(",");
            for (String imagePath : paths) {
                String trimmedPath = imagePath.trim();
                if (!trimmedPath.isEmpty()) {
                    addImageToDiv(div, trimmedPath, font);
                }
            }

            imageCell.add(div);
            table.addCell(imageCell);

        } catch (Exception e) {
            table.addCell(createErrorCell("图片加载失败: " + e.getMessage(), font));
        }
    }


    /**
     * 向指定的Div容器中添加图片元素
     * @param div 要添加图片的Div容器
     * @param imagePath 图片的相对路径
     * @param font 错误信息显示时使用的字体
     */
    private void addImageToDiv(Div div, String imagePath, PdfFont font) {
        try {
            // 构造图片的完整URL路径
            String fullPath = null;
            // 构造图片的完整URL路径
            if(!imagePath.startsWith("http") && !imagePath.startsWith("https")){
                fullPath=serverConfig.getUrl() + imagePath;
            }else{
                fullPath = imagePath;
            }

            // 验证路径有效性并加载图片
            if (fullPath != null && !fullPath.isEmpty()) {
                com.itextpdf.io.image.ImageData imageData = ImageDataFactory.create(fullPath);
                Image img = new Image(imageData);
                img.scaleToFit(300, 300);
                img.setMarginRight(50);
                div.add(img);
            }
        } catch (com.itextpdf.io.IOException ioException) {
            // 处理图片格式错误的情况
            div.add(createErrorParagraph("[图片格式错误]", font));
        } catch (Exception imgException) {
            // 处理图片加载失败的情况
            div.add(createErrorParagraph("[图片加载失败: " + imgException.getMessage() + "]", font));
        }
    }

    /**
     * 创建错误信息段落
     * @param text 错误信息文本
     * @param font 段落使用的字体
     * @return 包含错误信息的段落对象
     */
    private Paragraph createErrorParagraph(String text, PdfFont font) {
        return new Paragraph(text)
                .setFontColor(ColorConstants.RED)
                .setFont(font)
                .setMarginRight(50);
    }

    /**
     * 创建错误信息单元格
     * @param text 错误信息文本
     * @param font 单元格内文本使用的字体
     * @return 包含错误信息的表格单元格对象
     */
    private Cell createErrorCell(String text, PdfFont font) {
        return new Cell(1, 4)
                .add(new Paragraph(text)
                        .setFontColor(ColorConstants.RED)
                        .setFont(font)
                        .setTextAlignment(TextAlignment.CENTER));
    }


    /**
     * 格式化日期时间字符串
     * 将输入的日期时间字符串从一种格式转换为另一种格式
     *
     * @param dateTimeStr 输入的日期时间字符串，格式为 "EEE MMM dd HH:mm:ss z yyyy"
     * @return 格式化后的日期时间字符串，格式为 "yyyy-MM-dd HH:mm:ss"，如果输入为空则返回 "-"
     */
    private String formatDateTime(String dateTimeStr) {
        // 处理空值情况
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return "-";
        }

        // 定义输入和输出的日期格式
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", java.util.Locale.US);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 解析并重新格式化日期时间
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, inputFormatter);
        return dateTime.format(outputFormatter);
    }


    /**
     * 生成并保存PDF文件
     *
     * @param data PDF数据
     * @param originalFilename 原始文件名
     * @return 包含文件信息的Map
     * @throws Exception 异常
     */
    public Map<String, Object> generateAndSavePdf(Map<String, Object> data, String originalFilename) throws Exception {
        try {
            // 生成PDF内容
            byte[] pdfBytes = generatePdf(data);

            // 保存PDF文件
            String fileName = savePdf(pdfBytes, originalFilename);
            String url = serverConfig.getUrl() + fileName;

            Map<String, Object> result = new HashMap<>();
            result.put("url", url);
            result.put("fileName", fileName);
            result.put("newFileName", com.ruoyi.common.utils.file.FileUtils.getName(fileName));
            result.put("originalFilename", originalFilename);

            return result;
        } catch (Exception e) {
            throw new Exception("生成并保存PDF失败: " + e.getMessage(), e);
        }
    }
    /**
     * 保存PDF文件
     *
     * @param pdfBytes PDF字节数据
     * @param originalFilename 原始文件名
     * @return 文件名
     * @throws Exception 异常
     */
    public String savePdf(byte[] pdfBytes, String originalFilename) throws Exception {
        try {
            // 创建自定义 MultipartFile 实例
            MultipartFile multipartFile = new CustomMultipartFile(
                    pdfBytes,
                    "file",
                    originalFilename,
                    "application/pdf"
            );

            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();

            // 使用 FileUploadUtils 上传文件
            String fileName = FileUploadUtils.upload(filePath, multipartFile);
            return fileName;
        } catch (Exception e) {
            throw new Exception("保存PDF文件失败: " + e.getMessage(), e);
        }
    }
}
