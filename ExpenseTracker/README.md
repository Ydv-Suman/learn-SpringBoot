# ExpenseTracker

## Database Migration

This project uses Flyway for explicit, command-driven schema updates.
The schema is defined entirely in `V1__create_initial_schema.sql`.

Run migrations with:

```bash
./mvnw flyway:migrate
```

If your database connection is different from the defaults in `pom.xml`, override it like this:

```bash
./mvnw -Dflyway.url=jdbc:mysql://HOST:3306/DB -Dflyway.user=USER -Dflyway.password=PASS flyway:migrate
```

## Startup Behavior

- Hibernate is set to `validate`, so the app checks the schema at startup.
- It does not create or update tables automatically when you build or run the app.
- If you already have an older Flyway history table from previous migrations, use a fresh database or drop the existing schema before running the consolidated `V1`.

## MySQL Access Fix

If you see `Access denied for user 'root'@'192.168.65.1'`, the MySQL container needs remote root access.

For a fresh container, `compose.yml` already sets `MYSQL_ROOT_HOST=%`.

If the database volume was created before that setting existed, run this once inside the MySQL container:

```bash
docker exec -it expensetracker mysql -uroot -p
```

Then execute:

```sql
CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED BY '192113';
GRANT ALL PRIVILEGES ON expensetrackerdb.* TO 'root'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;
```

## Suggested Flow

1. Create or reset the database.
2. Run `./mvnw flyway:migrate`.
3. Start the application.
