/**********************************************************************
Copyright (c) 2013 Andy Jefferson and others. All rights reserved.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Contributors:
    ...
**********************************************************************/
package mydomain.datanucleus;

import java.util.ArrayList;
import java.util.List;

import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.store.rdbms.mapping.java.JavaTypeMapping;
import org.datanucleus.store.rdbms.sql.SQLStatement;
import org.datanucleus.store.rdbms.sql.expression.NumericExpression;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;
import org.datanucleus.store.rdbms.sql.expression.StringExpression;
import org.datanucleus.store.rdbms.sql.method.SQLMethod;

/**
 * Expression handler to evaluate Long.valueOf({expression}).
 * Returns a NumericExpression.
 */
public class LongValueOfMethod implements SQLMethod
{
    public SQLExpression getExpression(SQLStatement stmt, SQLExpression expr, List<SQLExpression> args)
    {
        if (args == null || args.size() == 0)
        {
            throw new NucleusUserException("Cannot invoke Long.valueOf without an argument");
        }

        SQLExpression argExpr = (SQLExpression)args.get(0);
        if (argExpr == null)
        {
            throw new NucleusUserException("Cannot invoke Long.valueOf with null argument");
        }
        if (argExpr instanceof StringExpression)
        {
            JavaTypeMapping m = stmt.getSQLExpressionFactory().getMappingForType(long.class, true);
            List castTypes = new ArrayList();
            castTypes.add("INTEGER");
            List castArgs = new ArrayList();
            castArgs.add(argExpr);
            return new NumericExpression(stmt, m, "CAST", castArgs, castTypes);
        }
        else
        {
            throw new NucleusUserException("Argument for Long.valueOf has to be a StringExpression/StringLiteral - invalid to pass " + argExpr);
        }
    }
}
