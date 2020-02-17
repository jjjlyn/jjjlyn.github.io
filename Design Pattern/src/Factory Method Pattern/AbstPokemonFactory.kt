abstract class AbstPokemonFactory {
    fun spawn(species: String) : Pokemon{
        val pokemon = spawnPokemon()
        pokemon.species = species
        return pokemon
    } // factory method !!

    protected abstract fun spawnPokemon() : Pokemon // 추상 Factory를 상속하는 Factory에서 구현
}