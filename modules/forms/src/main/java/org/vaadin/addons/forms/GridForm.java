/*
 *
 *  * Licensed to the Apache Software Foundation (ASF) under one or more
 *  * contributor license agreements.  See the NOTICE file distributed with
 *  * this work for additional information regarding copyright ownership.
 *  * The ASF licenses this file to You under the Apache License, Version 2.0
 *  * (the "License"); you may not use this file except in compliance with
 *  * the License.  You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.vaadin.addons.forms;

import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.GridLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GridForm extends Form {

    private final GridLayout layout;
    private final GridConstraints constraints = new GridConstraints();

    public GridForm() {
        this(1, 1);
    }

    public GridForm(int cols, int rows) {
        super(new GridLayout(cols, rows));
        this.layout = (GridLayout)getLayout();
    }

    public void setSpacing(boolean spacing) {
        this.layout.setSpacing(spacing);
    }

    public void setConstraints(GridConstraints gridConstraints) {
        if (gridConstraints != null) {
            final List<Object> visibleProperties = new ArrayList<Object>();
            synchronized (this.constraints) {
                this.constraints.clear();
                for (Map.Entry<Object, GridConstraint> entry : gridConstraints.entrySet()) {
                    visibleProperties.add(entry.getKey());
                    this.constraints.addConstraint(entry.getKey(), entry.getValue());
                }
            }
            setVisibleItemProperties(visibleProperties);
        }
    }

    /**
     * Adds the field to the form layout using the specified {@link GridConstraint}, if it exists.
     * <p>
     * The field is added to the form layout in the position specified by a GridConstraint, if it exists;
     * otherwise, if and only if there are zero {@link GridConstraint}s the field is added using
     * {@link com.vaadin.ui.Layout#addComponent(com.vaadin.ui.Component)}.
     * </p>
     *
     * <p>
     * Override this method to control how the fields are added to the layout.
     * </p>
     *
     * @param propertyId
     * @param field
     */
    @Override
    protected void attachField(Object propertyId, Field field) {
        if (propertyId == null || field == null) {
            return;
        }

        GridConstraint constraint = this.constraints.getContraint(propertyId);
        if (constraint != null) {
            final int startCol = constraint.getStartColumn();
            final int endCol = constraint.getEndColumn();
            final int startRow = constraint.getStartRow();
            final int endRow = constraint.getEndRow();
            if (startCol == endCol)
                this.layout.addComponent(field, startCol, startRow);
            else {
                field.setWidth("100%");
                this.layout.addComponent(field, startCol, startRow, endCol, endRow);
            }
        } else if (this.constraints.size() == 0)
            this.layout.addComponent(field);
    }
}
