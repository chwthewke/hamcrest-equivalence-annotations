package net.chwthewke.hamcrest.sut.specs;

import static net.chwthewke.hamcrest.equivalence.TextEquivalenceOption.IGNORE_CASE;
import net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn;
import net.chwthewke.hamcrest.annotations.Text;
import net.chwthewke.hamcrest.sut.classes.WithPublicProperty;

@EquivalenceSpecificationOn( WithPublicProperty.class )
public interface TextIgnoringCaseOnString {
    @Text( options = IGNORE_CASE )
    String getValue( );
}
