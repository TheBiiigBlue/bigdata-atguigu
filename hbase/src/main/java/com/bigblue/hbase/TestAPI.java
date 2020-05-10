package com.bigblue.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * @Author: TheBigBlue
 * @Description:
 * @Date: 2020/5/5
 */
public class TestAPI {

    /**
     * DDL
     *  1.创建命名空间
     *  2.表的增、删，判断表是否存在
     *
     * DML
     *  1.插入数据
     *  2.查数据，get
     *  3.查数据，scan
     *  4.删除数据
     */

    private static Admin admin;
    private static Connection conn;

    static {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "node01,node02,node03");
        try {
            conn = ConnectionFactory.createConnection(conf);
            admin = conn.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
//        System.out.println(isTableExists("stu1"));
//        createTable("stu1", "info1", "info2", "info3");
//        createTable("test:stu3", "info1", "info2", "info3");
//        dropTable("stu1");
//        createNameSpace("test");
//        putData("stu", "1001", "info1", "sex", "12");
//        System.out.println(getData("stu", "1001", "info1", "sex"));
//        scanData("stu");
        delData("stu", "1001", "info1", "sex");
        close();
    }

    /**
      * @Author: TheBigBlue
      * @Description: 表是否存在
      * @Date: 2020/5/5
      * @Param:
      * @return:
      **/
    public static boolean isTableExists(String tableName) throws IOException {
        boolean result = admin.tableExists(TableName.valueOf(tableName));
        admin.close();
        return result;
    }

    /**
      * @Author: TheBigBlue
      * @Description: 创建表
      * @Date: 2020/5/5
      * @Param:
      * @return:
      **/
    public static void createTable(String tableName, String... cfs) throws IOException {
        if(cfs == null || cfs.length == 0) {
            System.out.println("请设置列族信息");
            return;
        }
        if(isTableExists(tableName)){
            System.out.println(tableName + "表已存在");
            return;
        }
        HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf(tableName));
        for (String cf : cfs) {
            tableDesc.addFamily(new HColumnDescriptor(cf));
        }
        admin.createTable(tableDesc);
    }

    /**
      * @Author: TheBigBlue
      * @Description: 删除表
      * @Date: 2020/5/5
      * @Param:
      * @return:
      **/
    public static void dropTable(String tableName) throws IOException {
        if (!isTableExists(tableName)) {
            System.out.println(tableName + "表不存在");
            return;
        }
        admin.disableTable(TableName.valueOf(tableName));
        admin.deleteTable(TableName.valueOf(tableName));
    }

    /**
      * @Author: TheBigBlue
      * @Description: 创建命名空间
      * @Date: 2020/5/5
      * @Param:
      * @return:
      **/
    public static void createNameSpace(String nameSpace)  {
        try {
            admin.createNamespace(NamespaceDescriptor.create(nameSpace).build());
        } catch (NamespaceExistException ex) {
            ex.printStackTrace();
            System.err.println("命名空间已存在");
        }catch (IOException e) {
            e.printStackTrace();
            System.err.println("创建命名空间异常");
        }
    }

    /**
      * @Author: TheBigBlue
      * @Description: 创建表
      * @Date: 2020/5/5
      * @Param:
      * @return:
      **/
    public static void putData(String tableName, String rowKey, String colFamily, String colName, String value) throws IOException {
        Table table = conn.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(rowKey));
        put.addColumn(Bytes.toBytes(colFamily), Bytes.toBytes(colName), Bytes.toBytes(value));
        table.put(put);
        table.close();
    }

    /**
      * @Author: TheBigBlue
      * @Description: 获取数据
      * @Date: 2020/5/5
      * @Param:
      * @return:
      **/
    public static String getData(String tableName, String rowKey, String colFamily, String colName) throws IOException {
        Table table = conn.getTable(TableName.valueOf(tableName));
        Get get = new Get(Bytes.toBytes(rowKey));
        Result result = table.get(get);
        Cell directCell = result.getColumnLatestCell(Bytes.toBytes(colFamily), Bytes.toBytes(colName));
        String resultValue = Bytes.toString(CellUtil.cloneValue(directCell));
        for (Cell cell : result.rawCells()) {
            System.out.println("CF: " + Bytes.toString(CellUtil.cloneFamily(cell))
                    + "\tCN: " + Bytes.toString(CellUtil.cloneQualifier(cell))
                    + "\tV: " + Bytes.toString(CellUtil.cloneValue(cell)));
        }
        return resultValue;
    }

    /**
      * @Author: TheBigBlue
      * @Description: 扫描表，也可以扫描某一rowkey数据
      * @Date: 2020/5/5
      * @Param:
      * @return:
      **/
    public static void scanData(String tableName) throws IOException {
        Table table = conn.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        ResultScanner resultScanner = table.getScanner(scan);
        for (Result result : resultScanner) {
            for (Cell cell : result.rawCells()) {
                System.out.println("RK: " + Bytes.toString(CellUtil.cloneRow(cell))
                        + "\tCF: " + Bytes.toString(CellUtil.cloneFamily(cell))
                        + "\tCN: " + Bytes.toString(CellUtil.cloneQualifier(cell))
                        + "\tV: " + Bytes.toString(CellUtil.cloneValue(cell)));
            }
        }
        table.close();
    }

    /**
      * @Author: TheBigBlue
      * @Description: 删除数据
      * @Date: 2020/5/5
      * @Param:
      * @return:
      **/
    public static void delData(String tableName, String rowKey, String colFamily, String colName) throws IOException{
        Table table = conn.getTable(TableName.valueOf(tableName));
        Delete delete = new Delete(Bytes.toBytes(rowKey));
        //Delete the latest version of the specified column.
        //慎用
        delete.addColumn(Bytes.toBytes(colFamily), Bytes.toBytes(colName));
        //Delete all versions of the specified column.
        delete.addColumns(Bytes.toBytes(colFamily), Bytes.toBytes(colName));
        table.delete(delete);
        table.close();
    }

    public static void close() {
        try {
            if(admin != null){
                admin.close();
            }
            if(conn != null){
                conn.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
