package org.esa.beam.framework.gpf;

import com.thoughtworks.xstream.io.xml.xppdom.Xpp3Dom;

/**
 * A converter for the parameters of an operator.
 *
 * @author Norman Fomferra
 */
public interface ParameterConverter {

    /**
     * Sets the parameter values of the given operator using the <code>configuration</code>
     * given as a tree of (XML) elements.</p>
     * <p/>
     * <p>The <code>configuration</code> should exactly conform to the XML representation
     * generated by the {@link #getParameterValues(Operator,com.thoughtworks.xstream.io.xml.xppdom.Xpp3Dom)}
     * method.</p>
     *
     * @param operator      the operator which receives the parameter values
     * @param configuration the configuration from which provides the parameter values
     * @throws OperatorException if the conversion failes
     */
    void setParameterValues(Operator operator, Xpp3Dom configuration) throws OperatorException;

    /**
     * Gets the parameter values of the given operator as <code>configuration</code>
     * given as a tree of (XML) elements.
     * <p/>
     * <p>Implementors are free to choose an appropriate XML representation
     * of their parameter set.</p>
     *
     * @param operator      the operator which receives the parameter values
     * @param configuration the configuration from which provides the parameter values
     * @throws OperatorException if the conversion failes
     */
    void getParameterValues(Operator operator, Xpp3Dom configuration) throws OperatorException;
}
