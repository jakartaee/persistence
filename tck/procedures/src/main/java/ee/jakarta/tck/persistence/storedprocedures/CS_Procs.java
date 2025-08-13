/*
 * Copyright (c) 2011, 2024 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package ee.jakarta.tck.persistence.storedprocedures;

import java.sql.*;

public class CS_Procs {

  public static void GetEmpOneFirstNameFromOut(String[] out_param)
      throws SQLException {
    // out_param - OUT parameter containing the first name of employee 1

    String query = "SELECT FIRSTNAME FROM EMPLOYEE WHERE ID=1";
    Connection con = DriverManager.getConnection("jdbc:default:connection");
    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery(query);

    if (rs.next()) {
      out_param[0] = rs.getString(1);
    } else {
      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;

  }

  public static void GetEmpFirstNameFromOut(int in_param, String[] out_param)
      throws SQLException {
    // in_param - IN parameter containing the id value
    // out_param - OUT parameter containing the FirstName returned by the select

    String query = "SELECT FIRSTNAME FROM EMPLOYEE WHERE ID=" + in_param;
    Connection con = DriverManager.getConnection("jdbc:default:connection");
    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery(query);

    if (rs.next()) {
      out_param[0] = rs.getString(1);
    } else {
      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;

  }

  public static void GetEmpLastNameFromInOut(String[] inout_param)
      throws SQLException {
    // inout_param - (in - is the ID to look up, out is the last name returned
    // by the select)

    String query = "SELECT LASTNAME FROM EMPLOYEE WHERE ID="
        + Integer.parseInt(inout_param[0]);
    Connection con = DriverManager.getConnection("jdbc:default:connection");
    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery(query);

    if (rs.next()) {
      inout_param[0] = rs.getString(1);
    } else {
      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;

  }

  public static void GetEmpASCFromRS(ResultSet[] rs) throws SQLException {
    // rs - resultSet of all the ID's returned by the select

    String query = "SELECT ID, FIRSTNAME, LASTNAME, HIREDATE, SALARY FROM EMPLOYEE ORDER BY ID ASC";
    Connection con = DriverManager.getConnection("jdbc:default:connection");

    PreparedStatement pstmt = con.prepareStatement(query);
    rs[0] = pstmt.executeQuery();
    con.close();
    con = null;

  }

  public static void GetEmpIdFNameLNameFromRS(int in_param, ResultSet[] rs)
      throws SQLException {
    // in_param - IN parameter containing the id value
    // rs - resultSet of one ID returned by the select

    String query = "SELECT ID, FIRSTNAME, LASTNAME FROM EMPLOYEE WHERE ID="
        + in_param;

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement pstmt = con.prepareStatement(query);
    rs[0] = pstmt.executeQuery();
    con.close();
    con = null;
  }

  /*
   * public static void GetTwoRS(int in_param, ResultSet[] rs1, ResultSet[] rs2)
   * throws SQLException { // rs - resultSet of all the ID's returned by the
   * select
   * 
   * String query1 = "SELECT ID, FIRSTNAME, LASTNAME FROM EMPLOYEE WHERE ID=" +
   * in_param; String query2 =
   * "SELECT INSID, CARRIER FROM INSURANCE WHERE INSID=" + in_param;
   * 
   * Connection con = DriverManager.getConnection("jdbc:default:connection");
   * 
   * PreparedStatement pstmt1 = con.prepareStatement(query1); rs1[0] =
   * pstmt1.executeQuery();
   * 
   * PreparedStatement pstmt2 = con.prepareStatement(query2); rs2[0] =
   * pstmt2.executeQuery();
   * 
   * con.close(); con = null;
   * 
   * }
   * 
   * public static void GetRSAndOut(int in_param, String[] out_param,
   * ResultSet[] rs) throws SQLException { // in_param - IN parameter containing
   * the id value // rs - resultSet of one ID returned by the select
   * 
   * String query1 = "SELECT ID, FIRSTNAME, LASTNAME FROM EMPLOYEE WHERE ID=" +
   * in_param; String query2 = "SELECT FIRSTNAME from EMPLOYEE WHERE ID=" +
   * in_param;
   * 
   * 
   * Connection con = DriverManager.getConnection("jdbc:default:connection");
   * PreparedStatement pstmt = con.prepareStatement(query1); rs[0] =
   * pstmt.executeQuery();
   * 
   * Statement stmt = con.createStatement(); ResultSet rs1 =
   * stmt.executeQuery(query2);
   * 
   * if (rs1.next()) { out_param[0] = rs1.getString(1); } else { throw new
   * SQLException("Data not found"); }
   * 
   * stmt.close(); stmt = null; con.close(); con = null; }
   */

  public static void GetEmpIdUsingHireDateFromOut(Date in_param,
      int[] out_param) throws SQLException {
    // date_param - IN parameter containing the DB name
    // out_param - OUT parameter containing the ID returned by the select

    String query = "SELECT ID FROM EMPLOYEE WHERE HIREDATE='" + in_param + "'";

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery(query);

    if (rs.next()) {
      out_param[0] = rs.getInt(1);
    } else {
      throw new SQLException("Data not found");
    }

    rs.close();
    rs = null;
    stmt.close();
    stmt = null;
    con.close();
    con = null;

  }

  public static void UpdateEmpSalaryColumn() throws SQLException {

    String query = "UPDATE EMPLOYEE SET SALARY=0.00";

    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con.prepareStatement(query);

    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;

  }

  public static void DeleteAllEmp() throws SQLException {

    String query = "DELETE FROM EMPLOYEE";
    Connection con = DriverManager.getConnection("jdbc:default:connection");
    PreparedStatement ps = con.prepareStatement(query);

    ps.executeUpdate();

    ps.close();
    ps = null;
    con.close();
    con = null;

  }

  public static String ReplaceString(String origin_string, String old_string, String new_string) {
    return origin_string.replace(old_string, new_string);
  }
}
