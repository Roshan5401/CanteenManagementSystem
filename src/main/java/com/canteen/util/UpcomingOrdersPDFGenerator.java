package com.canteen.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.canteen.entities.OrderEntity;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

public class UpcomingOrdersPDFGenerator {
	public void generate(List <OrderEntity> orderslist, HttpServletResponse response) throws DocumentException, IOException {
	    // Creating the Object of Document
	    Document document = new Document(PageSize.A4);
	    // Getting instance of PdfWriter
	    PdfWriter.getInstance(document, response.getOutputStream());
	    // Opening the created document to change it
	    document.open();
	    // Creating font
	    // Setting font style and size
	    Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
	    fontTiltle.setSize(20);
	    // Creating paragraph
	    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");  
	    LocalDateTime now = LocalDateTime.now(); 
	    Paragraph paragraph1 = new Paragraph("List of the Upcoming Orders" +"   "+dtf.format(now), fontTiltle);
	     
	   // Paragraph paragraph2 = new Paragraph("Date and Time"+dtf.format(now), fontTiltle);
	    // Aligning the paragraph in the document
	    paragraph1.setAlignment(Paragraph.ALIGN_CENTER);
	    // Adding the created paragraph in the document
	    document.add(paragraph1);
	    // Creating a table of the 4 columns
	    PdfPTable table = new PdfPTable(5);
	    // Setting width of the table, its columns and spacing
	    table.setWidthPercentage(100f);
	    table.setWidths(new int[] {3,3,3,3,3});
	    table.setSpacingBefore(5);
	    // Create Table Cells for the table header
	    PdfPCell cell = new PdfPCell();
	    // Setting the background color and padding of the table cell
	    cell.setBackgroundColor(CMYKColor.BLUE);
	    cell.setPadding(5);
	    // Creating font
	    // Setting font style and size
	    Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
	    font.setColor(CMYKColor.WHITE);
	    // Adding headings in the created table cell or  header
	    // Adding Cell to table
	    cell.setPhrase(new Phrase("User name", font));
	    table.addCell(cell);
	    cell.setPhrase(new Phrase("Order Date", font));
	    table.addCell(cell);
	   cell.setPhrase(new Phrase("Order Id", font));
	    table.addCell(cell);
	    cell.setPhrase(new Phrase("Item Name", font));
	    table.addCell(cell);
	    cell.setPhrase(new Phrase("Price", font));
	    table.addCell(cell);
	    // Iterating the list of students
	    for (OrderEntity order: orderslist) {
	      // Adding student id
	    	
	    	
	    		 table.addCell(String.valueOf(order.getCanteenUsers().getName()));
	   	    
	   	      table.addCell(String.valueOf(order.getOrderDate()));

	   	     table.addCell(String.valueOf(order.getOrderId()));

	   	      table.addCell(order.getFood().getName());
	   	      
	   	      table.addCell(String.valueOf(order.getTotalPrice()));
	    	
	     
	    }
	    // Adding the created table to the document
	    document.add(table);
	    // Closing the document
	    document.close();
	  }
	}
