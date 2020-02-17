class PikachuFactory : AbstPokemonFactory() {
    override fun spawnPokemon(): Pokemon {
        return Pikachu()
    }
}