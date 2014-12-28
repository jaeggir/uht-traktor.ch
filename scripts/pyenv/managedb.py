#! bin/python

import sys
import getpass
import psycopg2 as pg
from clint import arguments
from clint.textui import colored, puts, columns

def getcursor(dbname, use_superuser=False):
    username = "flyway"
    if use_superuser:
        username = getpass.getuser()
     # Setup db connection
    conn = pg.connect("dbname=" + dbname + " user="+ username +" password=uht")
    cursor = conn.cursor()

    # Set connection to autocommit mode as we cannot modify the db from within a transaction
    conn.autocommit = True

    return cursor

def closeconnections(cursor):
    # Terminate all the other connections
    cursor.execute("SELECT pg_terminate_backend(pg_stat_activity.pid) \
                    FROM pg_stat_activity \
                    WHERE pg_stat_activity.datname = 'uht' \
                    AND pid <> pg_backend_pid();")

def dropdb(cursor):
    # Kill the db
    try:
        cursor.execute("DROP DATABASE uht;")

    except (pg.ProgrammingError, pg.OperationalError) as e:
        puts(colored.red("[Error]") + " " + "error dropping, psycopg says: "+e.message)
        sys.exit()

def createdb(cursor):
    # Recreate the db
    try:

        cursor.execute("CREATE DATABASE uht;")
        cursor.execute("GRANT ALL PRIVILEGES ON DATABASE uht to flyway;")
        cursor.execute("GRANT CONNECT ON DATABASE uht to uht;")
        cursor.execute("GRANT SELECT, INSERT, DELETE, UPDATE, USAGE ON ALL TABLES IN SCHEMA public TO uht;")

    except (pg.ProgrammingError, pg.OperationalError) as e:
        puts(colored.red("[Error]") + " " + "error creating, psycopg says: " + e.message)
        sys.exit()


def printHelp():
    puts("UHT DB Management script")
    puts()
    puts("Available commands: ")
    puts()
    puts(columns(["setupuser", 20], ["Prints the SQL needed to create the fueloptim user properly. Recommended usage: ./managedb.py setupuser | psql postgres", 60]))
    puts(columns(["create", 20], ["Creates the initial database from scratch.", 60]))
    puts(columns(["drop", 20], ["Drops the database (without recreating it)", 60]))
    puts(columns(["reset", 20], ["Drops the existing database and recreates a new one from scratch", 60]))
    puts()
    puts("Available flags:")
    puts()
    puts(columns(["-f", 20], ["Force-closes all existing connections when used together with reset or drop", 60]))
    puts()
    sys.exit()

if __name__ == '__main__':

    # Setup arguments
    all_args = arguments.Args()
    flags = all_args.flags
    args = arguments.Args([p for p in all_args.all if (p not in flags.all)], no_argv=True)

    # Print help
    if "--help" in flags:
        printHelp()

    # Check what we should do
    firstarg = args.get(0)
    if firstarg == "setupuser":
        # Just print the SQL to be piped into `psql postgres`
        print ("CREATE USER flyway WITH PASSWORD 'flyway' SUPERUSER;"),
        print ("ALTER USER flyway WITH CREATEDB;"),
        print ("CREATE USER uht WITH PASSWORD 'uht';")
        sys.exit()

    elif firstarg == "create":
        cursor = getcursor("postgres")
        createdb(cursor)

    elif firstarg == "drop":
        cursor = getcursor("postgres")
        if  "-f" in flags:
            closeconnections(getcursor("postgres", use_superuser=True))
        dropdb(cursor)

    elif firstarg == "reset":
        cursor = getcursor("postgres")
        if  "-f" in flags:
            closeconnections(getcursor("postgres", use_superuser=True))
        dropdb(cursor)
        createdb(cursor)

    else:
        puts("Invalid command")
        printHelp()
        sys.exit()

    puts(colored.green("Success!")) 
