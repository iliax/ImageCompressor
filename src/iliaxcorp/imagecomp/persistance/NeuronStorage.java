package iliaxcorp.imagecomp.persistance;

import java.util.List;

import iliaxcorp.imagecomp.Neuron;

public interface NeuronStorage {

	abstract public Neuron getNeuronByIndex(int inx);
	
	abstract public int getNeuronsCount();
	
	abstract public void setNeuron(int indx, Neuron n);
	
	abstract public void init();
	
	abstract public List<Neuron> getNeurons();
	
	abstract public void persist();
	
	abstract public void clear();
}
