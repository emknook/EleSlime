module EleSlime {
    requires com.google.gson;
    requires hanyaeger;
    requires javafx.graphics;
    requires javafx.controls;

    exports nl.han.jefmk;

    opens nl.han.jefmk.levels.model to com.google.gson;

    opens levels;
    opens sprites;
}