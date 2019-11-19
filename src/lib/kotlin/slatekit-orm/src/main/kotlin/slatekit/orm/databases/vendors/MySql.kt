package slatekit.orm.databases.vendors

import slatekit.common.db.IDb
import slatekit.common.naming.Namer
import slatekit.entities.repos.SqlRepo
import slatekit.query.Query
import slatekit.entities.Entity
import slatekit.entities.core.EntityInfo
import slatekit.orm.core.Converter
import slatekit.orm.core.SqlBuilder
import slatekit.orm.core.TypeMap
import slatekit.meta.models.Model
import slatekit.orm.OrmMapper

/**
 * MySql to Java types
 * https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-reference-type-conversions.html
 */
object MySqlTypeMap : TypeMap()


/**
 * Contains all the converters for each type
 * Only customizations form the common one go here
 */
class MySqlConverter<TId, T> : Converter<TId, T>() where TId : kotlin.Comparable<TId>, T : Any


class MySqlBuilder(namer: Namer?) : SqlBuilder(MySqlTypeMap, namer)


class MySqlQuery : Query()


/**
 * Repository class specifically for MySql
 * @param db
 * @tparam T
 */
open class MySqlEntityRepo<TId, T>(db: IDb, info:EntityInfo, mapper: OrmMapper<TId, T>)
    : SqlRepo<TId, T>(db, info, mapper) where TId : Comparable<TId>, T : Any {

    private val ormMapper = mapper

    override fun create(entity: T): TId {
        return ormMapper.insert(entity)
    }

    override fun update(entity: T): Boolean {
        return ormMapper.update(entity)
    }
}


/**
 * Maps an entity to sql and from sql records.
 *
 * @param model
 */
open class MySqlEntityMapper<TId, T>(
        model: Model,
        db:IDb,
        info:EntityInfo)
    : OrmMapper<TId, T>(model, db, MySqlConverter(), info)
        where TId : Comparable<TId>, T : Entity<TId>
