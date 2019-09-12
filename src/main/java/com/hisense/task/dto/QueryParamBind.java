package com.hisense.task.dto;

import com.hisense.task.utils.SqlConstant;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author Maybe
 */
public class QueryParamBind {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static void bindExpress(StringBuffer hsql, List<QueryParam> queryParamList) {
        if (queryParamList == null) {
            return;
        }
        if (!hsql.toString().toLowerCase().contains(SqlConstant.WHERE_KEY)) {
            hsql.append(" where 1=1 ");
        }
        int i = 1;
        for (QueryParam queryParam : queryParamList) {
            if (isInValid(queryParam)) {
                continue;
            }
            if ("like".equalsIgnoreCase(queryParam.getLogic())) {
                queryParam.setField("lower(" + queryParam.getField() + ")");
                hsql.append(" and ");
                hsql.append(queryParam.getField()).append(SqlConstant.SPACE).
                        append(queryParam.getLogic()).append(SqlConstant.COLON).append("_param_").append(i);
            } else if ("is".equalsIgnoreCase(queryParam.getLogic())) {
                hsql.append(" and ");
                hsql.append(queryParam.getValue()).append(SqlConstant.SPACE);
            } else if ("between".equalsIgnoreCase(queryParam.getLogic())) {
                hsql.append(" and ");
                hsql.append("to_char(").append(queryParam.getStartField()).append(",'yyyy-MM-dd')")
                        .append(SqlConstant.SPACE).append("<=").append(SqlConstant.COLON).append("_param_").append(i);
                hsql.append(" and ");
                hsql.append("to_char(").append(queryParam.getEndField()).append(",'yyyy-MM-dd')")
                        .append(SqlConstant.SPACE).append(">=").append(SqlConstant.COLON).append("_param_").append(i);
            } else if ("intext".equalsIgnoreCase(queryParam.getLogic())) {
                if (queryParam.getValue().trim().split(" ").length == 1) {
                    hsql.append(SqlConstant.AND_KEY)
                            .append("lower(").append(queryParam.getField()).append(")")
                            .append(" like ")
                            .append(SqlConstant.COLON)
                            .append("_param_").append(i);
                } else {
                    hsql.append(SqlConstant.AND_KEY)
                            .append(queryParam.getField())
                            .append(" in ")
                            .append(SqlConstant.COLON)
                            .append("_param_").append(i);
                }

            } else if ("in".equalsIgnoreCase(queryParam.getLogic())) {
                hsql.append(" and to_char(")
                        .append(queryParam.getField()).append(")")
                        .append(SqlConstant.SPACE)
                        .append(queryParam.getLogic())
                        .append(SqlConstant.COLON)
                        .append("_param_").append(i);
            } else if (queryParam.getType().equals(QueryFieldEnum.dateType.getValue())) {
                hsql.append(" and ");
                hsql.append("to_char(")
                        .append(queryParam.getField())
                        .append(",'yyyy-MM-dd')")
                        .append(SqlConstant.SPACE)
                        .append(queryParam.getLogic())
                        .append(SqlConstant.COLON)
                        .append("_param_").append(i);
            } else {
                hsql.append(" and ")
                        .append(queryParam.getField())
                        .append(SqlConstant.SPACE)
                        .append(queryParam.getLogic())
                        .append(SqlConstant.COLON)
                        .append("_param_").append(i);
            }
            i++;
        }
    }

    public static void setParam(Query query, List<QueryParam> queryParamList) {
        if (queryParamList == null || queryParamList.isEmpty()) {
            return;
        }
        int i = 1;
        for (QueryParam queryParam : queryParamList) {
            if (isInValid(queryParam)) {
                continue;
            }
            bind(query, queryParam, "_param_" + i);
            i++;
        }
    }

    private static boolean isInValid(QueryParam queryParam) {
        return (StringUtils.isBlank(queryParam.getValue())
                && queryParam.getValues().length == 0) ||
                StringUtils.isBlank(queryParam.getField()) ||
                StringUtils.isBlank(queryParam.getLogic());
    }

    private static void bind(Query query, QueryParam queryParam, String aliasName) {
        if (isInValid(queryParam)) {
            return;
        }
        if ("is".equalsIgnoreCase(queryParam.getLogic())) {
            return;
        }
        if ("intext".equalsIgnoreCase(queryParam.getLogic())) {
            String[] strings = queryParam.getValue().trim().split(" ");
            if (strings.length == 1) {
                query.setString(aliasName, "%" + strings[0].toLowerCase() + "%");
            } else {
                query.setParameterList(aliasName, strings);
            }

            return;
        }
        if ("in".equalsIgnoreCase(queryParam.getLogic())) {
            query.setParameterList(aliasName, queryParam.getValues());
            return;
        }

        if (StringUtils.isBlank(queryParam.getType())
                || queryParam.getType().equals(QueryFieldEnum.stringType.getValue())
                || QueryFieldEnum.stringType.getValue().equals(queryParam.getQueryType())) {
            if ("like".equalsIgnoreCase(queryParam.getLogic())) {
                query.setString(aliasName, "%" + queryParam.getValue().toLowerCase() + "%");
            } else {
                query.setString(aliasName, queryParam.getValue().replace("-", ""));
            }
        } else if (queryParam.getType().equals(QueryFieldEnum.longType.getValue())) {
            query.setLong(aliasName, Long.valueOf(queryParam.getValue()));
        } else if (queryParam.getType().equals(QueryFieldEnum.doubledType.getValue())) {
            query.setString(aliasName, queryParam.getValue());
        } else if (queryParam.getType().equals(QueryFieldEnum.dateType.getValue())) {
            query.setString(aliasName, queryParam.getValue());
        } else if (queryParam.getType().equals(QueryFieldEnum.nyaSelectType.getValue())) {
            query.setString(aliasName, queryParam.getValue());
        } else if (queryParam.getType().equals(QueryFieldEnum.dateTimeType.getValue())) {
            try {

                query.setDate(aliasName, DATE_FORMAT.parse(queryParam.getValue()));
            } catch (ParseException e) {
                e.printStackTrace();
                throw new RuntimeException("查询参数@" + queryParam.getField() + "的时间格式不对,格式应为:yyyy-MM-dd HH:mm:ss");
            }
        } else if (queryParam.getType().equals(QueryFieldEnum.dateMonthType.getValue())) {
            query.setString(aliasName, queryParam.getValue());
        } else {
            throw new RuntimeException("查询参数的数据类型应为:" + QueryFieldEnum.getValues());
        }
    }
}
