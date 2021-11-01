There is a producer main class and a consumer main class. The producer is run from within a project, and the consumer is run from a runnable jar file onan EC2 instance.
The source files are in src\main\java\cscie90\alex\hw6

Both rely on a profile named ‘developer’ in the default credentials file in the default locations.

[developer]
aws_access_key_id = ******
aws_secret_access_key = *****


Additionally, this project requires the java-json.jar which is not included in this source code.