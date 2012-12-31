package iliaxcorp.imagecomp.algs;

import java.util.Random;

import iliaxcorp.imagecomp.Color;
import iliaxcorp.imagecomp.Neuron;
import iliaxcorp.imagecomp.persistance.InMemoryNeuronStorage;
import iliaxcorp.imagecomp.persistance.NeuronStorage;

public class StorageInitializer extends Alg<NeuronStorage, Void> {

	private NeuronStorage ns = new InMemoryNeuronStorage();
	
	public StorageInitializer(int aNeuronCount, int blockSize) {
		int max = 100000;
		Random rand = new Random();
		for(int i=0; i<aNeuronCount; i++){
			Neuron n = new Neuron();
			
			for(int j=0; j < blockSize*blockSize; j++){
				n.setLinkColor(j, ( new Color((-1)*rand.nextInt(max) - 5000)));
			}
			
			ns.setNeuron(i, n);
		}
	}
	
	@Override
	public NeuronStorage processAlg(Void _v) {
		return ns;
	}

}
