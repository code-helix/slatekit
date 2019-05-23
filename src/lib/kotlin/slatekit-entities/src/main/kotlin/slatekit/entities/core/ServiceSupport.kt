package slatekit.entities.core

import slatekit.entities.Entity
import slatekit.entities.EntityRepo
import java.util.*

interface ServiceSupport<TId, T> where TId : Comparable<TId>, T : Entity<TId> {

    fun repo(): IEntityRepo
    fun repoT(): EntityRepo<TId, T>

    /**
     * Hook for derived to apply any other logic/field changes before create/update
     * @param mode
     * @param entity
     * @return
     */
    fun applyFieldData(mode: EntityAction, entity: T): T {
        return entity
    }


    fun isCreated(id: TId): Boolean {
        return when (id) {
            is Int -> id > 0
            is Long -> id > 0L
            is String -> !id.isEmpty()
            is UUID -> !id.toString().trim().isEmpty()
            else -> false
        }
    }
}
