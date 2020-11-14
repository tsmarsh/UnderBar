# Crypto

Because cryptography should be pretty easy.

This project was spawned out of a repeated requirement that I've seen on multiple projects in the last 12 months.

> Data must be encrypted in flight and at rest

HTTPS tends to solve the in flight requirement, but at rest is tricky. Some datastores do it:

* Amazon S3
* PostgresQL
* MS SQL Server

Even then, you frequently have to either trust someone else with your keys (S3), or the datastores implementation is too slow... lets clarify that, if a SQL Store was a poor fit for your use-cases without encryption it will be a terrible fit with encryption. 

If you want to use Redis, MongoDB, Cassandra, Datomic or any of the other stores you are responsible for encryption.

# Gotchas

The gotchas with encryption are:

* Padding
* Base64

With padding, you have to make sure you are consistently encrypting 