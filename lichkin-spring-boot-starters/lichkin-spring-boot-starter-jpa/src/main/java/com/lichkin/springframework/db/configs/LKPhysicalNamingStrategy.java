package com.lichkin.springframework.db.configs;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import com.lichkin.framework.db.utils.LKDBUtils;

/**
 * Entity映射命名策略，驼峰命名规则，并以T_开头。
 * 例：
 *
 * <pre>
 * T_USER      == UserEntity
 * T_USER_DEPT == UserDeptEntity
 * </pre>
 *
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public class LKPhysicalNamingStrategy implements PhysicalNamingStrategy {

	@Override
	public Identifier toPhysicalCatalogName(final Identifier name, final JdbcEnvironment jdbcEnvironment) {
		return name;
	}


	@Override
	public Identifier toPhysicalSchemaName(final Identifier name, final JdbcEnvironment jdbcEnvironment) {
		return name;
	}


	@Override
	public Identifier toPhysicalTableName(final Identifier name, final JdbcEnvironment jdbcEnvironment) {
		if (name == null) {
			return null;
		}

		return Identifier.toIdentifier(LKDBUtils.toPhysicalTableName(name.getText()));
	}


	@Override
	public Identifier toPhysicalSequenceName(final Identifier name, final JdbcEnvironment jdbcEnvironment) {
		return name;
	}


	@Override
	public Identifier toPhysicalColumnName(final Identifier name, final JdbcEnvironment jdbcEnvironment) {
		if (name == null) {
			return null;
		}
		return Identifier.toIdentifier(LKDBUtils.toPhysicalColumnName(name.getText()));
	}

}
