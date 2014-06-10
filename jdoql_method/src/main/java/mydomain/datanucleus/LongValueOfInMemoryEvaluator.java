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

import org.datanucleus.exceptions.NucleusException;
import org.datanucleus.query.QueryUtils;
import org.datanucleus.query.expression.InvokeExpression;
import org.datanucleus.query.expression.Literal;
import org.datanucleus.query.expression.ParameterExpression;
import org.datanucleus.query.expression.PrimaryExpression;
import org.datanucleus.query.inmemory.InMemoryExpressionEvaluator;
import org.datanucleus.query.inmemory.InvocationEvaluator;

/**
 * Evaluator for the method "Long.valueOf({stringExpr})".
 */
public class LongValueOfInMemoryEvaluator implements InvocationEvaluator
{
    /* (non-Javadoc)
     * @see org.datanucleus.query.evaluator.memory.InvocationEvaluator#evaluate(org.datanucleus.query.expression.InvokeExpression, org.datanucleus.query.evaluator.memory.InMemoryExpressionEvaluator)
     */
    public Object evaluate(InvokeExpression expr, Object invokedValue, InMemoryExpressionEvaluator eval)
    {
        if (invokedValue != null)
        {
            throw new NucleusException("Attempt to invoke Long.valueOf on " + invokedValue + " but this is a static method!");
        }

        // Evaluate the first argument
        String arg1 = null;
        Object arg1Obj = null;
        Object param = expr.getArguments().get(0);
        if (param instanceof PrimaryExpression)
        {
            PrimaryExpression primExpr = (PrimaryExpression)param;
            arg1Obj = eval.getValueForPrimaryExpression(primExpr);
        }
        else if (param instanceof ParameterExpression)
        {
            ParameterExpression paramExpr = (ParameterExpression)param;
            arg1Obj = QueryUtils.getValueForParameterExpression(eval.getParameterValues(), paramExpr);
        }
        else if (param instanceof Literal)
        {
            arg1Obj = ((Literal)param).getLiteral();
        }
        else if (param instanceof InvokeExpression)
        {
            arg1Obj = eval.getValueForInvokeExpression((InvokeExpression) param);
        }
        else
        {
            throw new NucleusException("Long.valueOf(...) requires arg of type String, but was " + param + " - unsupported");
        }
        arg1 = QueryUtils.getStringValue(arg1Obj);
        if (!(arg1 instanceof String))
        {
            throw new NucleusException("Long.valueOf(...) requires arg of type String, but was " + arg1 + " - unsupported");
        }

        return Long.valueOf(arg1);
    }
}
