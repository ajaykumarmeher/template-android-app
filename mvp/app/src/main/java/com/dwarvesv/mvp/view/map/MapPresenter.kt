package {{packageName}}.view.map

class MapPresenter(val view: MapContract.View) : MapContract.Presenter {

    init {
        view.presenter = this
    }


}
