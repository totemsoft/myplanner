/*
 *              Argus Software Pty Ltd License Notice
 * 
 * The contents of this file are subject to the Argus Software Pty Ltd
 * License Version 1.0 (the "License"). 
 * You may not use this file except in compliance with the License.
 * A updated copy of the License is available at
 * http://www.argussoftware.net/license/license_agreement.html
 * 
 * The Original Code is argus. The Initial Developer of the Original
 * Code is Argus Software Pty Ltd, All Rights Reserved.
 */

package com.argus.financials.myplanner.report.word;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Argus Software Pty Ltd</p>
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version                   $Revision: 1.8 $
 */

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;

import javax.swing.table.TableModel;

import com.argus.activex.ComException;
import com.argus.activex.microsoft.word9.Application;
import com.argus.activex.microsoft.word9.Document;
import com.argus.activex.microsoft.word9.Documents;
import com.argus.activex.microsoft.word9.Find;
import com.argus.activex.microsoft.word9.InlineShape;
import com.argus.activex.microsoft.word9.InlineShapes;
import com.argus.activex.microsoft.word9.Range;
import com.argus.activex.microsoft.word9.Row;
import com.argus.activex.microsoft.word9.Rows;
import com.argus.activex.microsoft.word9.Selection;
import com.argus.activex.microsoft.word9.Table;
import com.argus.activex.microsoft.word9.Tables;
import com.argus.activex.microsoft.word9.View;
import com.argus.activex.microsoft.word9.WdColorIndex;
import com.argus.activex.microsoft.word9.WdFindWrap;
import com.argus.activex.microsoft.word9.WdReplace;
import com.argus.activex.microsoft.word9.Window;
import com.argus.financials.myplanner.report.ReportException;

public class WordReportJava2COM 
    extends WordReportBase 
    implements Runnable
{

    private static final Logger logger = Logger.getLogger(WordReportJava2COM.class.getName());

    /**
     * 
     */
    public WordReportJava2COM() {
        super();
    }

    
    public void run() {
    
        try {
            super.run();
        } catch (Exception ex) {
            // TODO: exception handling
            throw new RuntimeException(ex);
        }

    }
    
    /**
     * 
     */
    protected void doReport() throws ReportException {

        Application app = new Application();
        try {
            //Bookmark bookmarkTOC = null;
        
            Documents docs = app.getDocuments();
            String template = getTemplate();
            String report = getReport();
            String [] subReports = getSubReport();
            
            //get main report orientation
            Document reportDocument;
            if (template != null) {
                reportDocument = docs.Add( template );
                
                checkFile( report );
                reportDocument.getContent().InsertFile( report );
            } else {
                if (report != null) {
                    reportDocument = docs.Open(report, Boolean.TRUE, Boolean.TRUE); //read only
                } else {
                    reportDocument = docs.Add();
                }
            }
            //int orientation = reportDocument.getPageSetup().getOrientation();
            //reportDocument.getPageSetup().setOrientation(orientation);
            //reportDocument.addDocumentEventsListener( this );
            
            // sub-reports
            for ( int i = 0; subReports != null && i < subReports.length; i++ ) {
                append( reportDocument, subReports[i] );
            }
            
            reportDocument.Activate();
            Window window = reportDocument.getActiveWindow();
            
            View view = window.getView();
            // Note: If the active view isn't either outline view or master document view,
            // an error occurs when add to subdocuments.
            //view.setType( WdViewType.wdOutlineView );
            //view.setType( WdViewType.wdMasterView );
            
            view.getZoom().setPercentage( IReportConstants.DEFAULT_ZOOM );

            // it looks like sometimes word do not update styles
            if (template != null)
                reportDocument.CopyStylesFromTemplate(template);

            update( reportDocument );
            
            if (getSavedReport() != null) {
                reportDocument.SaveAs(getSavedReport());
                reportDocument.Close();
                app.Quit();
                app = null;
            }
            
        } catch (ComException ex) {
            throw new ReportException(ex);
        } finally {
            if (app != null) {
                app.setVisible(true);
                app.Activate();
            }
        }
        
    }

    
    private void append( Document doc, String fileName ) 
        throws ReportException, ComException 
    {
        checkFile( fileName );
        
        Range range = doc.Range();
        range.setStart( range.getEnd() );
        range.InsertFile( fileName );
        
        doc.Range().InsertParagraphAfter();
        
    }
    
    private void update( Document doc ) 
        throws ReportException, ComException 
    {
        // 1. replace all (client) parameters with data
        logger.fine( "1. replace all (client) parameters with data" );
        
        // 2. table parameters
        if ( doc.getTables().getCount() > 0 ) {
            // 2.1 table parameters ( e.g. []/ in tables )
            logger.fine( "2.1 table collection parameters" );
            updateCollection( doc );
        }
        
        // 3. single parameters
        logger.fine( "3. single parameters" );
        updateParameter( doc );

        // 4.1 remove logical conditions brakets (where parameters are not replaced yet)
        logger.fine( "4.1 remove logical conditions brackets (where parameters are not replaced yet)" );
        updateLogicalConditions( getIfRange( doc.Range() ) );
        
        // 4.2. delete logical conditions (any range really)
        logger.fine( "4.2 delete logical conditions" );
        delete( getIfRange( doc.Range() ) );
        
        // 5. highlight or delete all missing parameters
        logger.fine( "5. highlight all missing parameters" );
        highlight( getParameterRange( doc.Range() ), WdColorIndex.wdYellow );
        //delete( getParametersRange( doc.Range() ) );
        
    }

    private void updateParameter( Document doc )
        throws ReportException, ComException 
    {
        java.util.Stack params = getParameterRange( doc.Range() );
        if ( params == null || params.size() == 0 ) return;
    
        while ( !params.empty() ) {
            final Range range = (Range) params.pop();
            String paramName = getParameterName( range );
            if (paramName == null) continue;
            
            java.lang.Object value = getValue( paramName );
            //logger.fine( "Parameter value: " + value );
                
            if (value != null) {
                String sValue = value.toString();
                File file = new File(sValue);
                if (file.exists()) {
                    //range.setText(sValue);
                    updateParameter(doc, range, file);
                } else {
                    range.setText(sValue);
                }
            }
                
        }
        
    }

    private void updateCollection( Document doc )
        throws ReportException, ComException 
    {
        java.util.Stack params = getParameterRange( doc.Range() );
        if ( params == null || params.size() == 0 ) return;

        while ( !params.empty() ) {
            final Range range = (Range) params.pop();
            String paramName = getParameterName( range );
            if (paramName == null) continue;
            
            int index = paramName.indexOf( IReportConstants.COLLECTION );
            if ( index < 0 ) continue;
            
            String collectionName = paramName.substring( 0, index );
            logger.fine( "Parameter: " + paramName + ", Collection: " + collectionName );

            java.lang.Object value = getValue( paramName );
            if ( value == null ) {
                value = getValue( collectionName );
            }
            //logger.fine( "Parameter value: " + value );

            //logger.fine( "...matching parameter: " + paramName + ", class: " + value.getClass().getName() + ", value: " + value );
            if (value == null) {
                if ( !updateParameter( doc, collectionName, getCollection(collectionName) ) ) {
                    logger.warning( "FAILED to update collection parameter ..." );
                    continue;
                }
            } else if (value instanceof Collection) {
                if ( !updateParameter( doc, collectionName, (Collection) value ) ) {
                    logger.warning( "FAILED to update Collection parameter ..." );
                    continue;
                }
            } else if (value instanceof TableModel) {
                if ( !updateParameter( doc, collectionName, getCollection(collectionName, (TableModel) value) ) ) {
                    logger.warning( "FAILED to update TableModel parameter ..." );
                    continue;
                }
            } else {
                logger.severe( "Unhandled parameter type: " + value.getClass().getName() );
            }
                
        }
        
    }
        
    // single file parameter ( e.g. image file name )
    // this is file path (we have to insert file image)
    private boolean updateParameter(Document doc, Range range, File file) 
        throws ReportException, ComException 
    {
        String findText = range.getText();
        //logger.fine( "range: " + findText + ", value: " + file );
        InlineShape shape = addPicture( doc.getInlineShapes(), file, range );
        return shape != null;
    }
    
    // ALL Collection parameters will be displayed as tables
    // table parameter ( e.g. [Client/Income[]/Amount] )
    private boolean updateParameter(
        Document doc,
        final String collectionName, // e.g. Client/Income
        java.util.Collection collection ) 
        throws ReportException, ComException 
    {
        int addRows = collection.size();
        logger.fine( "Collection parameters: " + collectionName + ", addRows: " + addRows );

        java.util.Map map = getParameter( collectionName, collection );
        if ( map == null ) return false;
        
        Tables tables = doc.getTables();
        
        for ( int t = 1; t <= tables.getCount(); t++ ) {
            Table table = tables.Item(t);
            // find table(s) where this parameter (collection) is used
            if ( !table.getRange().getFind().Execute( collectionName ) )
                continue;
            
            //logger.fine( "!!!!!!!!!! table found: " + table );
            Rows rows = table.getRows();
            for ( int r = 1; r <= rows.getCount(); r++ ) {
                Row row = rows.Item(r);
                // find row(s) where this parameter (collection) is used
                if ( !row.getRange().getFind().Execute( collectionName ) )
                    continue;
                
                // empty collection, lets delete this row and continue search
                if ( addRows == 0 ) {
                    row.Delete();
                    continue;
                }
                
                // row found, lets copy it
                //logger.fine( "!!!!!!!!!! row to copy found: " + row );
                Range rowRange = row.getRange();
                rowRange.Copy();
                
                // duplicate this row number of times (first row already exists)
                for ( int i = addRows; i > 0 ; i-- ) {
                    // create new Range object at the end of current range, and paste original range (row)
                    Range range = rowRange.getDuplicate();
                    if (i != addRows)
                        range.Paste();
                    
                    if ( !range.getFind().Execute(
                            IReportConstants.COLLECTION, // Object FindText
                            Boolean.FALSE, // Object MatchCase
                            Boolean.FALSE, // Object MatchWholeWord
                            Boolean.FALSE, // Object MatchWildcards
                            Boolean.FALSE, // Object MatchSoundsLike
                            Boolean.FALSE, // Object MatchAllWordForms
                            Boolean.TRUE, // Object Forward
                            new Integer( WdFindWrap.wdFindContinue ), // Object Wrap
                            Boolean.FALSE, // Object Format
                            "[" + i + "]/", // Object ReplaceWith
                            new Integer( WdReplace.wdReplaceAll ))) // Object Replace
                    {
                        // TODO: ???
                        //logger.warning("Row [" + i + "] No collection parameter found (expected format: Client/Dependents[]/FullName).");
                    }

                }
                
                // !!! only one row per table can have this <paramName>
                break; 
                
            }
            
            //logger.fine( "Update collection parameter: " + paramName + ", values: " + map );
            rows = table.getRows();
            for ( int r = 1; r <= rows.getCount(); r++ ) {
                Row row = rows.Item(r);
                // confirm - this is row where this parameter (from collection) is used
                if ( !row.getRange().getFind().Execute( collectionName ) )
                    continue;
                
                // get all parameters within this row range
                // eg <wr:param name=�Client/Dependents[1]/FullName�/>
                java.util.Stack params = getParameterRange( row.getRange() );
                if ( params == null ) continue;
                
                while ( !params.isEmpty() ) {
                    final Range range = (Range) params.pop();
                    String param = getParameterName( range );
                    //logger.fine( param );
                    if (param == null) continue;

                    Object value = map.get( param );
                    //logger.fine( param + "=" + value );
                    if ( value == null ) continue;
                    
                    if ( value instanceof java.lang.String ) {
                        range.setText( (java.lang.String) value);
                    } else {
                        throw new ComException( "Invalid value: " + value + ", java.lang.String expected." );
                    }
                    
                }
                
            }
            
        }
        
        return true;
        
    }
        
    // ALL Collection parameters will be displayed as tables
    // table parameter ( e.g. [Client/Income[]/Amount] )
    private boolean updateTable(
        Document doc,
        String collectionName,
        int addRows ) 
        throws ReportException, ComException 
    {
        Tables tables = doc.getTables();
        
        for ( int t = 1; t <= tables.getCount(); t++ ) {
            Table table = tables.Item(t);
            // find table(s) where this parameter (collection) is used
            if ( !table.getRange().getFind().Execute( collectionName ) )
                continue;
            
            //logger.fine( "!!!!!!!!!! table found: " + table );
            Rows rows = table.getRows();
            for ( int r = 1; r <= rows.getCount(); r++ ) {
                Row row = rows.Item(r);
                // find row(s) where this parameter (collection) is used
                if ( !row.getRange().getFind().Execute( collectionName ) )
                    continue;
                
                // empty collection, lets delete this row and continue search
                if ( addRows == 0 ) {
                    row.Delete();
                    continue;
                }
                
                // row found, lets copy it
                //logger.fine( "!!!!!!!!!! row to copy found: " + row );
                Range rowRange = row.getRange();
                rowRange.Copy();
                
                // duplicate this row number of times (first row already exists)
                for ( int i = addRows; i > 0 ; i-- ) {
                    // create new Range object at the end of current range, and paste original range (row)
                    Range range = rowRange.getDuplicate();
                    if (i != addRows)
                        range.Paste();
                    
                    if ( !range.getFind().Execute(
                            IReportConstants.COLLECTION, // Object FindText
                            Boolean.FALSE, // Object MatchCase
                            Boolean.FALSE, // Object MatchWholeWord
                            Boolean.FALSE, // Object MatchWildcards
                            Boolean.FALSE, // Object MatchSoundsLike
                            Boolean.FALSE, // Object MatchAllWordForms
                            Boolean.TRUE, // Object Forward
                            new Integer( WdFindWrap.wdFindContinue ), // Object Wrap
                            Boolean.FALSE, // Object Format
                            "[" + i + "]/", // Object ReplaceWith
                            new Integer( WdReplace.wdReplaceAll ))) // Object Replace
                    {
                        // TODO: ???
                        //logger.warning("Row [" + i + "] No collection parameter found (expected format: Client/Dependents[]/FullName).");
                    }

                }
                
                // !!! only one row per table can have this <paramName>
                break; 
                
            }
            
        }
        
        return true;
        
    }
        
    /**
     * 
     * @param stack of conditionals
     */
    private void updateLogicalConditions( java.util.Stack stack ) {
        
        while ( stack != null && !stack.empty() ) {
            Range range = (Range) stack.pop();
            String test = getIfTest(range);
                
            if ( evaluateExpression( test ) ) {
                
                // remove test wrapper only
                range.getFind().Execute(
                    IReportConstants.IF_TEST, // Object FindText
                    Boolean.FALSE, // Object MatchCase
                    Boolean.FALSE, // Object MatchWholeWord
                    Boolean.TRUE, // Object MatchWildcards
                    Boolean.FALSE, // Object MatchSoundsLike
                    Boolean.FALSE, // Object MatchAllWordForms
                    Boolean.TRUE, // Object Forward
                    new Integer( WdFindWrap.wdFindStop ), // Object Wrap wdFindContinue
                    Boolean.FALSE, // Object Format
                    "", // Object ReplaceWith
                    new Integer( WdReplace.wdReplaceOne ) // Object Replace
                );
                
                range.getFind().Execute(
                    IReportConstants.IF_BODY_END, // Object FindText
                    Boolean.FALSE, // Object MatchCase
                    Boolean.FALSE, // Object MatchWholeWord
                    Boolean.TRUE, // Object MatchWildcards
                    Boolean.FALSE, // Object MatchSoundsLike
                    Boolean.FALSE, // Object MatchAllWordForms
                    Boolean.TRUE, // Object Forward
                    new Integer( WdFindWrap.wdFindStop ), // Object Wrap
                    Boolean.FALSE, // Object Format
                    "", // Object ReplaceWith
                    new Integer( WdReplace.wdReplaceOne ) // Object Replace
                );
                
            }
                
        }
        
    }   
    
    
    /////////////////////////////////////////////////////////////////////////////////////
    // (e.g. [Partner/Person/OtherGivenNames] if Partner/Person/OtherGivenNames == null )
    /////////////////////////////////////////////////////////////////////////////////////
    private java.util.Stack getParameterRange( Range range ) {
        return getRange( range, IReportConstants.PARAMETER_RANGE );
    }
    
    /////////////////////////////////////////////////////////////////////////////////////
    // anything in form: {and} {.........}, or {} {.........}
    // (e.g. {and} {... [Partner/Person/FullName] ...} if Partner == null )
    // (e.g. {and} {... Valeri I. Shibaev ...} if Partner == null )
    /////////////////////////////////////////////////////////////////////////////////////
    private java.util.Stack getIfRange( Range range ) {
        return getRange( range, IReportConstants.IF_RANGE );
    }

    private java.util.Stack getRange( Range range, String findText ) {
        
        java.util.Stack stack = new java.util.Stack();
        
        Find find = range.getFind();
        find.ClearFormatting();

        //Object FindText, Object MatchCase, Object MatchWholeWord, Object MatchWildcards, Object MatchSoundsLike, Object MatchAllWordForms, Object Forward, Object Wrap, Object Format, Object ReplaceWith, Object Replace, Object MatchKashida, Object MatchDiacritics, Object MatchAlefHamza, Object MatchControl
        boolean found = find.Execute( findText, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE );
        while ( found ) {
            Object obj = range.getDuplicate();
            if ( !stack.contains( obj ) ) {
                stack.add( obj );
            }
            range.setStart(range.getEnd());
            
            found = find.Execute();
            
        }
        
        return stack;
        
    }

    
    private String getParameterName(final Range range) {
        return getValue(range, IReportConstants.PARAMETER_NAME_BEGIN, IReportConstants.PARAMETER_NAME_END);
    }
    
    private String getIfTest(final Range range) {
        return getValue(range, IReportConstants.IF_TEST_BEGIN, IReportConstants.IF_TEST_END);
    }
    
    private String getIfBody(final Range range) {
        return getValue(range, IReportConstants.IF_BODY_BEGIN, IReportConstants.IF_BODY_END);
    }
    
    private String getValue(final Range range, String beginText, String endText) {
        if ( range.getStart() == range.getEnd() ) 
            return null;

        Range r = range.getDuplicate();
        Find find = r.getFind();
        find.ClearFormatting();
        if (!find.Execute( beginText, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE )) {
            // TODO:???
            //highlight( range, WdColorIndex.wdYellow );
            //logger.warning("NOT FOUND: start");
            return null;
        }
        int start = r.getEnd() + 1; // remove ["]
        
        r = range.getDuplicate();
        find = r.getFind();
        find.ClearFormatting();
        if (!find.Execute( endText, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE )) {
            // TODO:???
            //highlight( range, WdColorIndex.wdYellow );
            //logger.warning("NOT FOUND: end");
            return null;
        }
        int end = r.getStart() - 1; // remove ["]
        
        r = range.getDuplicate();
        r.setStart(start); 
        r.setEnd(end); 
        return r.getText();
        
    }
    
    private void highlight( java.util.Stack stack, int color ) {
        while ( stack != null && !stack.empty() ) {
            highlight( (Range) stack.pop(), color );
        }
    }
    
    private void highlight( Range range, int color ) {
        range.setHighlightColorIndex( color );
    }

    private void delete( java.util.Stack stack ) {
        
        if ( stack == null ) return;
        
        while ( !stack.empty() ) {
            Object object = stack.pop();
            //logger.fine( "deleting " + object.getClass().getName() );
            if ( object instanceof Range )
                ( (Range) object ).Delete();
        }
        
    }

    private Table findTable( Document doc, String tableName ) {
        return findTable( doc, tableName, -1 );
    }

    private Table findTable( Document doc, String tableName, int columnCount ) {
    
        Tables tables = doc.getTables();
        if ( tables.getCount() == 0 )
            return null;
        
        Table tableFirst = tables.Item(1);
        if ( tables.getCount() == 1 )
            return tableFirst;
        
        Table tableByName = null;
        Table tableByColumnCount = null;
        
        for ( int t = 1; t <= tables.getCount(); t++ ) {
            Table table = tables.Item(t);
            // find table where this tableName is used
            if ( table.getRange().getFind().Execute( tableName ) ) {
                tableByName = table; 
                break;
            }
            
            if ( tableByColumnCount == null && table.getColumns().getCount() == columnCount )
                tableByColumnCount = table;
            
        }
            
        return tableByName == null ? ( tableByColumnCount == null ? tableFirst : tableByColumnCount ) : tableByName;
        
    }
/*
    private void modifyTable( Table table, javax.swing.table.TableModel model ) {
        
        long time = java.lang.System.currentTimeMillis();

        int originalRowsCount = table.getRows().getCount(); // rows [2..rowsCount] will be removed (used for formatting only)
        int originalColumnsCount = table.getColumns().getCount(); // columns [1..columnsCount]
        
        int vertAlign = table.Cell( 1, 1 ).getVerticalAlignment();
        int columnsCount = model.getColumnCount();
        if ( columnsCount > 15 )
            columnsCount = 15;
        for ( int c = 0; c < columnsCount; c++ ) {
            if ( c >= originalColumnsCount )
                table.getColumns().Add();
            Cell cell = table.Cell( 1, c + 1 );
            cell.getRange().setText( model.getColumnName( c ) );
            cell.setVerticalAlignment( vertAlign );
        }

        //This code is to create dummy Selection object
        //Selection selection = wordApp.getSelection();
        
//if (DEBUG) java.lang.System.out.println( "model.getRowCount()=" + model.getRowCount() );
        for ( int r = 0; r < model.getRowCount(); r++ ) {
         
            int rowType = -1;
            Row copyRow = null;
            if ( model instanceof com.smoothlogic.swing.table.ISmartTableModel ) {

                ISmartTableRow row = ( ( com.smoothlogic.swing.table.ISmartTableModel ) model ).getRowAt( r );
                if ( row == null ) {
                    java.lang.System.err.println( "Word8::modifyTable( " + model + " ) : getRowAt( " + r + " ) == null" );
                    continue;
                }
                rowType = row.getRowType();

                copyRow = getRow( table.getRows(), rowType, originalRowsCount );
                
                if ( copyRow == null ) {
                         if ( rowType == ISmartTableRow.HEADER1 ) copyRow = originalRowsCount >= 2 ? table.getRows().Item(2) : null;
                    else if ( rowType == ISmartTableRow.BODY )    copyRow = originalRowsCount >= 3 ? table.getRows().Item(3) : null;
                    else if ( rowType == ISmartTableRow.FOOTER1 ) copyRow = originalRowsCount >= 4 ? table.getRows().Item(4) : null;
                }
                
                if ( copyRow == null )
                    java.lang.System.err.println( "copyRow=null, originalRowsCount=" + originalRowsCount + ", rowType=" + rowType );
                
            } else {
                copyRow = getRow( table.getRows(), ISmartTableRow.BODY, originalRowsCount );
                if ( copyRow == null )
                    copyRow = originalRowsCount >= 3 ? table.getRows().Item(3) : null;

                if ( copyRow == null )
                    java.lang.System.err.println( "copyRow=null, originalRowsCount=" + originalRowsCount );

            }
            
            //copyRow.Select();
            //selection.CopyFormat();
            //selection.PasteFormat();
            
            Range range = copyRow.getRange();
            range.Copy();
            
            // create new Range object at the end of current range, and paste original range (row)
            range = table.getRange();
            range.setStart( range.getEnd() );
            range.Paste();

            Cell copyCell = copyRow == null ? null : copyRow.getCells().Item(2);
            if ( copyCell != null )
                vertAlign = copyCell.getVerticalAlignment();

            for ( int c = 0; c < columnsCount; c++ ) {
                                
                // get javax.swing.table.TableModel value as String
                Object value = model.getValueAt( r, c );
                String sValue;
                if ( value == null )
                    sValue = "";
                else if ( value instanceof java.lang.Number )
                    sValue = number.toString( (java.lang.Number) value );
                else
                    sValue = value.toString();

                Cell cell = table.Cell( r + 1 + originalRowsCount, c + 1 );
                range = cell.getRange();
                range.setText( sValue );

                if ( copyCell != null && c >= 2 )
                    cell.setVerticalAlignment( vertAlign );

            }
                    
        }
        
        for ( int r = 2; r <= originalRowsCount; r++ )
            table.getRows().Item(2).Delete();
        
        if ( model.getColumnCount() < originalColumnsCount )
            for ( int c = 2; c <= originalColumnsCount; c++ )
                table.getColumns().Item(2).Delete();
        
if (DEBUG) java.lang.System.out.println( "modifyTable, time=" + ( java.lang.System.currentTimeMillis() - time ) );

    }
*/    

    private InlineShape addPicture( InlineShapes shapes, File file, Range range )
        throws ReportException, ComException 
    {
        try {
            // AddPicture does not replace range ???
            range.setText("");
            InlineShape shape = shapes.AddPicture(
                file.getCanonicalPath()   // String FileName
                , Boolean.FALSE  // Object LinkToFile (Optional Variant)
                , Boolean.TRUE   // Object SaveWithDocument (Optional Variant)
                , range          // Range Optional Variant.
                //The range where the picture will be placed in the text.
                //The picture replaces the range, if the range isn't collapsed.
                //If this argument is omitted, the picture is placed automatically.
            );
            return shape;
        } catch (IOException e) {
            throw new ReportException(e);
        }

//        Shape shape =
//            doc.getShapes().AddPicture(
//                file.getCanonicalPath()   // String FileName
//                //, Boolean.FALSE  // Object LinkToFile (Optional Variant)
//                //, Boolean.TRUE   // Object SaveWithDocument (Optional Variant)
//                //, new Integer(0)  // new com.ibm.bridge2java.Variant( new Object(), VTVOID),  // Object Left (Optional Variant)
//                //, new Integer(0)  // Object Top (Optional Variant)
//                //, new Integer(400)  // Object Width (Optional Variant)
//                //, new Integer(300)  // Object Height (Optional Variant)
//                //, range           // Object Anchor (Optional Variant)
//            );
//        // If the LockAnchor property for the shape is set to True,
//        // you cannot drag the anchor from its position on the page.
//        //shape.setLockAnchor( 0 );
    }
        
    
    /////////////////////////////////////////////////////////////////////////////////////////
    // utility methods
    /////////////////////////////////////////////////////////////////////////////////////////
    private int getOrientation( Application app, String fileName ) 
        throws ComException 
    {
        Document doc = app.getDocuments().Open( fileName, Boolean.FALSE, Boolean.TRUE );
        try {
            return doc.getPageSetup().getOrientation();
        } finally {
            doc.Close();
        }
        
    }
    private void setOrientation( Document doc, int orientation) 
        throws ComException 
    {
        doc.getPageSetup().setOrientation( orientation );
    }
    private static void setOrientation( Selection sel, int orientation ) 
        throws ComException 
    {
        sel.Select();
        sel.setOrientation( orientation );
    }

}
