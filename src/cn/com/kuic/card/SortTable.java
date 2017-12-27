package cn.com.kuic.card;

import java.util.*;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;

//tableEPCData=new SortTable();
//JScrollPane tableEPCScroll = new JScrollPane(tableEPCData);
//tableEPCScroll.setBounds(10, 127, 376, 160);
//getContentPane().add(tableEPCScroll);

@SuppressWarnings("serial")
public class SortTable extends JTable{
	public static int EL_STRING=0;
	public static int EL_PASSWORD=1;
	public static int EL_BOOL=2;
	public static int EL_INT=3;
	public static int EL_DOUBLE=4;
	public static int EL_COMBO=5;
	private DefaultTableModel mTableModel=null;
	
	public SortTable() {
		mTableModel=new DefaultTableModel();
		setModel(mTableModel);
		setAutoResizeMode(AUTO_RESIZE_OFF);
	}
	
	public SortTable(Object[][] rowData, Object[] columnNames) {
		mTableModel=new DefaultTableModel(rowData, columnNames);
		setModel(mTableModel);
		setAutoResizeMode(AUTO_RESIZE_OFF);
	}

	public int getRowCount(){
		return mTableModel.getRowCount();
	}
	
    public int getColumnCount(){
    	return mTableModel.getColumnCount();
    }
    
    public String getColumnName(int columnIndex){
    	return mTableModel.getColumnName(columnIndex);
    }
    
    public String GetString(int rowIndex, int columnIndex){
    	return mTableModel.getValueAt(rowIndex, columnIndex).toString();
    }
    
    public void SetString(int rowIndex, int columnIndex, String aValue){
    	mTableModel.setValueAt(aValue, rowIndex, columnIndex);
    }
	
	public void AppendColumn(String columnName)
	{
		AppendColumn(columnName,SortTable.EL_STRING,false);
	}
	
	public void AppendColumns(String[] columnNames)
	{
		for (int i = 0; i < columnNames.length; i++) {
			AppendColumn(columnNames[i],SortTable.EL_STRING,false);
		}
	}
	
	public void AppendColumn(String columnName, int colType)
	{
		AppendColumn(columnName,colType,false);
	}

	public void AppendColumn(String columnName, int colType, boolean editable)
	{
		mTableModel.addColumn(columnName);
	}
	
	public int AddNew()
	{
		String data[]=new String[mTableModel.getColumnCount()]; 
		mTableModel.addRow(data);
		return mTableModel.getRowCount()-1;
	}
	
	public int insertRow(int row)
	{
		String data[]=new String[mTableModel.getColumnCount()]; 
		mTableModel.insertRow(row, data);
		return row;
	}
	
	public void offsetColumnWidth(int columnIndex, int offsetColumnWidth)
	{
		TableColumn col=getColumnModel().getColumn(columnIndex);
		int orgWidth = col.getWidth();
		col.setPreferredWidth(orgWidth+offsetColumnWidth);
	}
	
	public void offsetColumnWidths(int[] offsetColumnWidths)
	{
		for (int i = 0; i < offsetColumnWidths.length; i++) {
			offsetColumnWidth(i, offsetColumnWidths[i]);
		}
	}
	
	public void setColumnWidth(int columnIndex, int columnWidth)
	{
		getColumnModel().getColumn(columnIndex).setPreferredWidth(columnWidth);
	}
	
	public void setColumnWidths(int []columnWidth)
	{
		for (int i = 0; i < columnWidth.length && i<getColumnModel().getColumnCount(); i++) {
			setColumnWidth(i, columnWidth[i]);
		}
	}
	
	public void selectRow(int rowIndex){
		setRowSelectionInterval(rowIndex,rowIndex);
	}
	
	public void DeleteAllItems(){
		DefaultTableModel model = (DefaultTableModel) getModel();
		for (int i = model.getRowCount()-1; i>=0; i--)
		{
			model.removeRow(i);
		}
	}
}

