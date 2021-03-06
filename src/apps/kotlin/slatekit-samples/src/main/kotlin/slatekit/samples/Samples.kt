package slatekit.samples

import kotlinx.coroutines.*
import slatekit.app.AppRunner
import slatekit.common.io.Alias
import slatekit.providers.logback.LogbackLogs


/**
 * Entry point into the sample console application with support for:
 *
 * 1. environment ( local, dev, qat, pro )
 * 2. command line args
 * 3. argument validation
 * 4. about / help / version display
 * 5. diagnostics ( on startup and end )
 * 6. logging ( console + logback )
 * 7. life-cycle events ( init, exec, end )
 *
 * java -jar ${app.name}.jar ?
 * java -jar ${app.name}.jar --about
 * java -jar ${app.name}.jar --version
 * java -jar ${app.name}.jar -env=dev
 * java -jar ${app.name}.jar -env=dev -log.level=info -conf.dir = "jars"
 * java -jar ${app.name}.jar -env=dev -log.level=info -conf.dir = "conf"
 * java -jar ${app.name}.jar -env=dev -log.level=info -conf.dir = "file://./conf-sample-batch"
 * java -jar ${app.name}.jar -env=dev -log.level=info -conf.dir = "file://./conf-sample-shell"
 * java -jar ${app.name}.jar -env=dev -log.level=info -conf.dir = "file://./conf-sample-server"
 *
 * slatekit new app -name="MyApp1" -package="company1.apps"
 * slatekit new api -name="MyAPI1" -package="company1.apis"
 * slatekit new cli -name="MyCLI1" -package="company1.apps"
 * slatekit new env -name="MyApp2" -package="company1.apps"
 * slatekit new job -name="MyJob1" -package="company1.jobs"
 * slatekit new lib -name="MyLib1" -package="company1.libs"
 * slatekit new orm -name="MyApp1" -package="company1.apps"
 *
 * -job.name=queued
 *
 * FUTURE:
 * 1. Support more args: -app.envs="dev,qat,stg,pro" -app.dest="some directory" -sk.version='0.9.28'
 *
 * CODEGEN:
 * 1. slatekit.codegen.toKotlin -templatesFolder="usr://dev/tmp/slatekit/slatekit/scripts/templates/codegen/kotlin" -outputFolder="usr://dev/tmp/codegen/kotlin" -packageName="blendlife"
 */
object Samples {
    fun app(args: Array<String>) {
        /**
         * DOCS : https://www.slatekit.com/arch/app/
         *
         * NOTES: The AppRunner does the following:
         *
         * 1. checks for command line args
         * 2. validates command line args against the Args schema ( optional )
         * 3. builds an AppContext for the app ( containing args, environment, config, logs )
         * 4. creates an App using supplied lambda ( Your Application instance )
         * 5. displays start up information and diagnostics using the Banner
         * 6. executes the life-cycle steps ( init, exec, done )
         */
        runBlocking {
            val sample = slatekit.samples.app.App
            AppRunner.run(
                    cls = Samples::class.java,
                    rawArgs = args,
                    about = sample.about,
                    schema = sample.schema,
                    enc = sample.encryptor,
                    logs = LogbackLogs(),
                    hasAction = true,
                    source = Alias.Jar,
                    builder = { ctx -> slatekit.samples.app.App(ctx) }
            )
        }
    }


    fun cli(args: Array<String>) {
        /**
         * DOCS : https://www.slatekit.com/arch/app/
         *
         * NOTES: The AppRunner does the following:
         *
         * 1. checks for command line args
         * 2. validates command line args against the Args schema ( optional )
         * 3. builds an AppContext for the app ( containing args, environment, config, logs )
         * 4. creates an App using supplied lambda ( Your Application instance )
         * 5. displays start up information and diagnostics using the Banner
         * 6. executes the life-cycle steps ( init, exec, done )
         */
        runBlocking {
            val sample = slatekit.samples.cli.App
            AppRunner.run(
                    cls = Samples::class.java,
                    rawArgs = args,
                    about = sample.about,
                    schema = sample.schema,
                    enc = sample.encryptor,
                    logs = LogbackLogs(),
                    hasAction = true,
                    source = Alias.Jar,
                    builder = { ctx -> slatekit.samples.cli.App(ctx) }
            )
        }
    }


    fun api(args: Array<String>) {
        /**
         * DOCS : https://www.slatekit.com/arch/app/
         *
         * NOTES: The AppRunner does the following:
         *
         * 1. checks for command line args
         * 2. validates command line args against the Args schema ( optional )
         * 3. builds an AppContext for the app ( containing args, environment, config, logs )
         * 4. creates an App using supplied lambda ( Your Application instance )
         * 5. displays start up information and diagnostics using the Banner
         * 6. executes the life-cycle steps ( init, exec, done )
         */
        runBlocking {
            val sample = slatekit.samples.api.App
            AppRunner.run(
                    cls = Samples::class.java,
                    rawArgs = args,
                    about = sample.about,
                    schema = sample.schema,
                    enc = sample.encryptor,
                    logs = LogbackLogs(),
                    hasAction = true,
                    source = Alias.Jar,
                    builder = { ctx -> slatekit.samples.api.App(ctx) }
            )
        }
    }


    fun job(args: Array<String>) {
        slatekit.samples.job.run(args)
    }
}