package slatekit.meta

import slatekit.common.EnumLike
import slatekit.common.Serializer
import slatekit.common.serialization.SerializerCsv
import slatekit.common.serialization.SerializerJson
import slatekit.common.serialization.SerializerProps


object Serialization {
    fun csv(isoDates:Boolean = false): SerializerCsv     = SerializerCsv(this::serializeObject, isoDates)
    fun json(isoDates:Boolean = false): SerializerJson   = SerializerJson(this::serializeObject, isoDates)
    fun sampler(isoDates:Boolean = false): Serializer   = SerializerSample(this::serializeObject, isoDates)
    fun props(prettyPrint:Boolean = false, isoDates:Boolean = false): SerializerProps = SerializerProps(prettyPrint, this::serializeObject, isoDates)


    /**
     * recursive serialization for a object.
     *
     * @param item: The object to serialize
     * @param serializer: The serializer to serialize a value to a string
     * @param delimiter: The delimiter to use between key/value pairs
     */
    fun serializeObject(serializer: Serializer, item: Any, depth: Int): Unit {

        // Handle enum
        if(Reflector.isSlateKitEnum(item.kClass)){
            val enumVal = (item as EnumLike).value
            serializer.serialize(enumVal)
            return
        }

        // Begin
        serializer.onContainerStart(item, Serializer.ParentType.OBJECT_TYPE, depth)

        // Get fields
        val fields = Reflector.getProperties(item.kClass)

        // Standardize the display of the props
        val maxLen = if (serializer.standardizeWidth) {
            fields.maxBy { it.name.length }?.name?.length ?: 0
        }
        else {
            0
        }

        fields.forEachIndexed { index, field ->
            // Get name/value
            val propName = field.name.trim()

            // Standardized width
            val finalPropName = if (serializer.standardizeWidth) {
                propName.padEnd(maxLen)
            }
            else {
                propName
            }
            val value = Reflector.getFieldValue(item, propName)

            // Entry
            serializer.onMapItem(item, depth, index, finalPropName, value)
        }

        // End
        serializer.onContainerEnd(item, Serializer.ParentType.OBJECT_TYPE, depth)
    }
}