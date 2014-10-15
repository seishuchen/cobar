/*
 * Copyright 1999-2012 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * (created at 2012-6-13)
 */
package com.alibaba.cobar.server.core.model;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.cobar.server.config.SchemasConfig;
import com.alibaba.cobar.server.core.route.rule.Rule;
import com.alibaba.cobar.server.util.SplitUtil;

/**
 * @author xianmao.hexm
 */
public class Schemas {

    private Map<String, Schema> schemas;

    public Schemas(SchemasConfig model) {
        schemas = new HashMap<String, Schemas.Schema>();
        for (SchemasConfig.Schema schema : model.getSchemaList()) {
            Schemas.Schema scs = new Schemas.Schema(schema);
            schemas.put(scs.getName(), scs);
        }
    }

    public Map<String, Schema> getSchemas() {
        return schemas;
    }

    public Schema getSchema(String name) {
        return schemas.get(name);
    }

    public static class Schema {
        private String name;
        private String[] dataNodes;
        private Rule rule;
        private boolean isSharding;

        public Schema(SchemasConfig.Schema model) {
            String name = model.getName();
            if (name != null) {
                this.name = name.trim();
            }
            String dataNodes = model.getDataNodes();
            if (dataNodes != null) {
                this.dataNodes = SplitUtil.split(dataNodes, ',', true);
            }
            String rule = model.getRule();
            if (rule != null) {
                try {
                    this.rule = (Rule) Class.forName(rule.trim()).newInstance();
                } catch (InstantiationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        public String getName() {
            return name;
        }

        public String[] getDataNodes() {
            return dataNodes;
        }

        public Rule getRule() {
            return rule;
        }

        public boolean isSharding() {
            return isSharding;
        }

        public void setSharding(boolean isSharding) {
            this.isSharding = isSharding;
        }

    }

}