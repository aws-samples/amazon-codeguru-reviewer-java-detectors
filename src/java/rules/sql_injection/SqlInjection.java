/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package rules.sql_injection;

import javax.servlet.http.HttpServletRequest;

public class SqlInjection {

    // {fact rule=sql-injection@v1.0 defects=1}
    public void executeSqlStatementNonCompliant(HttpServletRequest request, java.sql.Connection connection) {
        final String favoriteColor = request.getParameter("favoriteColor");
        try {
            // Noncompliant: user-given input might contain malicious special characters.
            String sql = "SELECT * FROM people WHERE favorite_color='" + favoriteColor + "'";
            java.sql.Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (java.sql.SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // {/fact}

    // {fact rule=sql-injection@v1.0 defects=0}
    public void executeSqlStatementCompliant(HttpServletRequest request, java.sql.Connection connection) {
        final String favoriteColor = request.getParameter("favoriteColor");
        // Compliant: user-given input is sanitized before use.
        if (!favoriteColor.matches("[a-z]+")) {
            throw new IllegalArgumentException();
        }
        try {
            String sql = "SELECT * FROM people WHERE favorite_color='" + favoriteColor + "'";
            java.sql.Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (java.sql.SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // {/fact}

}
