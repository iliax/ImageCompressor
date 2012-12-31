package iliaxcorp.imagecomp.persistance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import iliaxcorp.imagecomp.Neuron;
import iliaxcorp.imagecomp.utils.IOUtils;

public class InMemoryNeuronStorage implements NeuronStorage {

	private volatile List<Neuron> neurons = new ArrayList<Neuron>();

	private Map<String, String> params;

	public void setParams(Map<String, String> params) {
		if(params == null){
			throw new IllegalArgumentException();
		}
		this.params = params;
	}
	
	public InMemoryNeuronStorage(Map<String, String> params) {
		if(params == null){
			throw new IllegalArgumentException();
		}
		this.params = params;
	}

	public InMemoryNeuronStorage() {
	}
	
	@Override
	public Neuron getNeuronByIndex(int inx) {
		return neurons.get(inx);
	}

	@Override
	public int getNeuronsCount() {
		return neurons.size();
	}

	@Override
	public void setNeuron(int indx, Neuron n) {
		if (indx >= neurons.size()) {
			for (int i = 0; i < indx - neurons.size() + 1; i++) {
				neurons.add(null);
			}
		}
		neurons.set(indx, n);
	}

	@Override
	public void init() {
		neurons = IOUtils.deserializeObject(params.get("path"));
	}

	@Override
	public void persist() {
		IOUtils.serializeObject(params.get("path"), neurons);
	}

	@Override
	public void clear() {
		neurons.clear();
	}

	@Override
	public List<Neuron> getNeurons() {
		return neurons;
	}

}
