package com.uni.kitcheniq.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.uni.kitcheniq.models.PurchaseOrder;
import com.uni.kitcheniq.models.PurchaseOrderItem;
import com.uni.kitcheniq.repository.PurchaseOrderItemRepository;
import com.uni.kitcheniq.repository.PurchaseOrderRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class PdfService {

    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    public PdfService(PurchaseOrderItemRepository purchaseOrderItemRepository,
                      PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderItemRepository = purchaseOrderItemRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    public byte[] generatePurchaseOrderPdf(Long purchaseOrderId) {
        PurchaseOrder order = purchaseOrderRepository.findPurchaseOrderByIdWithItems(purchaseOrderId);
        if (order == null) {
            throw new RuntimeException("Purchase Order not found");
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            Paragraph title = new Paragraph().add(new Text("Purchase Order").setBold())
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 2}))
                    .useAllAvailableWidth();

            infoTable.addCell(new Cell().add(new Paragraph("Order ID:")).setBorder(Border.NO_BORDER));
            infoTable.addCell(new Cell().add(new Paragraph(order.getId().toString())).setBorder(Border.NO_BORDER));

            infoTable.addCell(new Cell().add(new Paragraph("Supplier ID:")).setBorder(Border.NO_BORDER));
            infoTable.addCell(new Cell().add(new Paragraph(order.getSupplier().getId().toString())).setBorder(Border.NO_BORDER));

            infoTable.addCell(new Cell().add(new Paragraph("Status:")).setBorder(Border.NO_BORDER));
            infoTable.addCell(new Cell().add(new Paragraph(order.getStatus().name())).setBorder(Border.NO_BORDER));

            document.add(infoTable);
            document.add(new Paragraph("\n"));

            Table itemsTable = new Table(UnitValue.createPercentArray(new float[]{4, 2, 2, 2}))
                    .useAllAvailableWidth();

            itemsTable.addHeaderCell(new Cell().add(new Paragraph(new Text("Item").setBold()))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY));
            itemsTable.addHeaderCell(new Cell().add(new Paragraph(new Text("Quantity").setBold()))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY));
            itemsTable.addHeaderCell(new Cell().add(new Paragraph(new Text("Unit Price").setBold()))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY));
            itemsTable.addHeaderCell(new Cell().add(new Paragraph(new Text("Subtotal").setBold()))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY));

            for (PurchaseOrderItem item : order.getItems()) {
                itemsTable.addCell(new Cell().add(new Paragraph(item.getInventoryItem().getName())));
                itemsTable.addCell(new Cell().add(new Paragraph(String.valueOf(item.getQuantity())))
                        .setTextAlignment(TextAlignment.RIGHT));
                itemsTable.addCell(new Cell().add(new Paragraph(item.getUnitPrice().toString()))
                        .setTextAlignment(TextAlignment.RIGHT));
                itemsTable.addCell(new Cell().add(new Paragraph(item.getSubTotalPrice().toString()))
                        .setTextAlignment(TextAlignment.RIGHT));
            }

            document.add(itemsTable);
            document.add(new Paragraph("\n"));

            Paragraph total = new Paragraph()
                    .add(new Text("Total Amount: ").setBold())
                    .add(new Text(order.getTotalAmount().toString()))
                    .setTextAlignment(TextAlignment.RIGHT);
            document.add(total);

            document.add(new Paragraph("\n\n"));

            Paragraph poweredBy = new Paragraph("Powered by KitchenIQ")
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(poweredBy);

            try {
                String logoUrl = "https://yjgqcxmmctekuovyelwo.supabase.co/storage/v1/object/sign/logos/main_logo." +
                        "png?token=eyJraWQiOiJzdG9yYWdlLXVybC1zaWduaW5nLWtleV83MjQwNTUzMS00MjQ5LTRjNzUtYjNlNC1kYTI" +
                        "2MTM1NzVkYzQiLCJhbGciOiJIUzI1NiJ9.eyJ1cmwiOiJsb2dvcy9tYWluX2xvZ28ucG5nIiwiaWF0IjoxNzU4" +
                        "NzI1NzMyLCJleHAiOjE3OTAyNjE3MzJ9.08I4UeeveJXuQ3rnxVo4LdeRE-y6SHIHyeaByY2ZIPE";
                ImageData data = ImageDataFactory.create(logoUrl);
                Image logo = new Image(data);

                logo.scaleToFit(120, 80);
                logo.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER);
                logo.setTextAlignment(TextAlignment.CENTER);

                document.add(logo);
            } catch (Exception e) {
                System.err.println("No se pudo cargar el logo: " + e.getMessage());
            }

            document.close();
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }
}
