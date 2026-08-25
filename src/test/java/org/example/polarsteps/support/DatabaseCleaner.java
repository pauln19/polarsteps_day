package org.example.polarsteps.support;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleaner {

	private final JdbcTemplate jdbcTemplate;

	DatabaseCleaner(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void clean() {
		List<String> tables = this.jdbcTemplate.queryForList("""
				SELECT table_name FROM information_schema.tables
				WHERE table_schema = 'public'
				  AND table_type = 'BASE TABLE'
				  AND table_name <> 'flyway_schema_history'
				""", String.class);

		if (tables.isEmpty()) {
			return;
		}

		String quoted = tables.stream().map(table -> "\"" + table + "\"").reduce((a, b) -> a + ", " + b).orElseThrow();
		this.jdbcTemplate.execute("TRUNCATE TABLE " + quoted + " RESTART IDENTITY CASCADE");
	}

}
