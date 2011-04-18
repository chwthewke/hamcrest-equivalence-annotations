package net.chwthewke.hamcrest.sut.specs;

import net.chwthewke.hamcrest.annotations.ApproximateEquality;
import net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn;
import net.chwthewke.hamcrest.sut.classes.WithDoubleProperty;

@EquivalenceSpecificationOn( WithDoubleProperty.class )
public interface ApproximateEqualityOnDouble {
    @ApproximateEquality( tolerance = 0.001d )
    double getValue( );
}
