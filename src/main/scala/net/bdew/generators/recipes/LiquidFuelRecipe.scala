package net.bdew.generators.recipes

import com.google.gson.JsonObject
import net.bdew.generators.registries.Recipes
import net.bdew.lib.recipes.{BaseMachineRecipe, BaseMachineRecipeSerializer, FluidIngredient}
import net.minecraft.item.crafting.{IRecipeSerializer, IRecipeType}
import net.minecraft.network.PacketBuffer
import net.minecraft.util.ResourceLocation

class LiquidFuelRecipeSerializer extends BaseMachineRecipeSerializer[LiquidFuelRecipe] {
  override def fromJson(recipeId: ResourceLocation, obj: JsonObject): LiquidFuelRecipe = {
    val fluid = FluidIngredient.fromJson(obj.get("fuel"))
    val fePerMb = obj.get("fePerMb").getAsFloat
    new LiquidFuelRecipe(recipeId, fluid, fePerMb)
  }

  override def fromNetwork(recipeId: ResourceLocation, buff: PacketBuffer): LiquidFuelRecipe = {
    val fluid = FluidIngredient.fromPacket(buff)
    val fePerMb = buff.readFloat()
    new LiquidFuelRecipe(recipeId, fluid, fePerMb)
  }

  override def toNetwork(buffer: PacketBuffer, recipe: LiquidFuelRecipe): Unit = {
    recipe.input.toPacket(buffer)
    buffer.writeFloat(recipe.fePerMb)
  }
}

class LiquidFuelRecipe(id: ResourceLocation, val input: FluidIngredient, val fePerMb: Float) extends BaseMachineRecipe(id) {
  override def getSerializer: IRecipeSerializer[_] = Recipes.liquidFuelSerializer.get()
  override def getType: IRecipeType[_] = Recipes.liquidFuelType
}
