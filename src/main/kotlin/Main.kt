fun main(){
    OffersRepository().getOffers("Krzesło").let { print(it) }
}