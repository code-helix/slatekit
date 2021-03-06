/**
<slate_header>
author: Kishore Reddy
url: www.github.com/code-helix/slatekit
copyright: 2016 Kishore Reddy
license: www.github.com/code-helix/slatekit/blob/master/LICENSE.md
desc: A tool-kit, utility library and server-backend
usage: Please refer to license on github for more info.
</slate_header>
 */

package slatekit.examples


//<doc:import_required>
import slatekit.entities.Entities
import slatekit.entities.core.EntityContext
//</doc:import_required>

//<doc:import_examples>
import slatekit.results.Try
import slatekit.results.Success
import slatekit.common.conf.ConfFuncs
import slatekit.common.data.DbConString
import slatekit.common.data.Connections
import slatekit.common.data.Vendor


import slatekit.db.Db
import slatekit.entities.core.EntityInfo
import slatekit.examples.common.User
import slatekit.examples.common.UserRepository
import slatekit.examples.common.UserService
import slatekit.meta.models.ModelMapper
import slatekit.orm.orm
import slatekit.orm.databases.vendors.MySqlMapper
import slatekit.orm.databases.vendors.MySqlRepo

//</doc:import_examples>


/**
 * Created by kreddy on 3/15/2016.
 */
class Example_Entities_Reg : Command("types") {

    override fun execute(request: CommandRequest): Try<Any> {
        //<doc:setup>
        // The entities are dependent on the database connections setup.
        // See Example_Database.kt for more info

        // 1. Register the default connection
        val dbs = Connections.of(ConfFuncs.readDbCon("user://.slate/db_default.txt")!!)

        // 2. Register a named connection
        //val dbs = Connections.named(("user_db", DbUtils.loadFromUserFolder(".slate", "db_default.txt"))

        // 3: Register connection as a shard and link to a group
        //val dbs = Connections.groupedDbs(("group1", List[(String,DbConString)](("shard1", DbUtils.loadFromUserFolder(".slate", "db_default.txt")))))
        //</doc:setup>

        //<doc:examples>
        // The entities can be registered and set up in multiple ways.
        // They can be registered to :
        //
        // - use in-memory repository or a sql ( mysql ) repository
        // - use the default EntityService[T] or a custom EntityService
        // - use a singleton instance or new instance
        // - use a certain type of database ( mysql only for now )
        // - use the default EntityRepository ( mysql ) or a custom repository
        // - use a supplied EntityMapper or a custom mapper
        val entities = Entities({ con -> Db(con) })

        // Case 1: In-memory
        showResults("Case 1", entities.prototype<User>(User::class))

        // Case 2: In-memory + with custom service
        showResults("Case 2", entities.prototype<User>(User::class, UserService::class))

        // Case 3: EntityService<TId, T>
        // NOTE: This is the Entities approach ( you handle the Repo/ Mapper implementation )
        showResults("Case 3", entities.register(
                User::class, Long::class, UserService(entities, UserRepository()), Vendor.Memory))

        // Case 4: ORM : EntityService<TId, T> with supplied MySqlRepo, OrmMapper
        showResults("Case 3", entities.orm<Long, User>(
                Vendor.MySql, User::class, Long::class, "users", UserService::class))

        // Case 5: Manual setup
        val con = DbConString("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/mydb", "user", "password")
        val db = Db(con)
        val model = ModelMapper.loadSchema(User::class)
        val mapper = MySqlMapper<Long, User>(model, db, EntityInfo(Long::class, User::class, "users"))
        val repo = MySqlRepo<Long, User>(db, EntityInfo(Long::class, User::class, "users"), mapper)
        val service = UserService(entities, repo)
        showResults("Case 3", entities.register(User::class, Long::class, service, Vendor.Memory))

        //</doc:setup>

        //<doc:examples>
        // Use case 1: Get repository
        val repo2 = entities.getRepo<Long, User>(User::class)

        // Use case 2: Get the service
        val svc = entities.getSvc<Long, User>(User::class)

        // Use case 3: Get the entity mapper
        val mapper2 = entities.getMapper<Long,User>(User::class)

        // Use case 4: Get the repo for a specific shard
        val repoShard = entities.getRepo<Long, User>(User::class)
        //</doc:examples>

        return Success("")
    }


    fun showResults(desc: String, regInfo: EntityContext) {
        println(desc)
        println()
    }
}
