package dcc.ufmg.anthill.mapred;

import java.io.IOException;

import java.util.AbstractMap.SimpleEntry;

import dcc.ufmg.anthill.Filter;
import dcc.ufmg.anthill.stream.StreamNotWritable;

public abstract class Mapper<InputType, SimpleEntry<Comparable, OutputType> >
extends Filter<InputType, SimpleEntry<Comparable, OutputType> > {
}
