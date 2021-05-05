import java.io.Serializable;
import java.util.ArrayList;
import java.text.*;
import java.lang.Math;

public class DecisionTree implements Serializable {

	DTNode rootDTNode;
	int minSizeDatalist; //minimum number of datapoints that should be present in the dataset so as to initiate a split

	// Mention the serialVersionUID explicitly in order to avoid getting errors while deserializing.
	public static final long serialVersionUID = 343L;

	public DecisionTree(ArrayList<Datum> datalist , int min) {
		minSizeDatalist = min;
		rootDTNode = (new DTNode()).fillDTNode(datalist);
	}

	class DTNode implements Serializable{
		//Mention the serialVersionUID explicitly in order to avoid getting errors while deserializing.
		public static final long serialVersionUID = 438L;
		boolean leaf;
		int label = -1;      // only defined if node is a leaf
		int attribute; // only defined if node is not a leaf
		double threshold;  // only defined if node is not a leaf

		DTNode left, right; //the left and right child of a particular node. (null if leaf)

		DTNode() {
			leaf = true;
			threshold = Double.MAX_VALUE;
		}


		// this method takes in a datalist (ArrayList of type datum). It returns the calling DTNode object
		// as the root of a decision tree trained using the datapoints present in the datalist variable and minSizeDatalist.
		// Also, KEEP IN MIND that the left and right child of the node correspond to "less than" and "greater than or equal to" threshold
		DTNode fillDTNode(ArrayList<Datum> datalist) {
			//ADD CODE HERE
			DTNode newNode = new DTNode();

			if(datalist.size() >= minSizeDatalist) {
				if(calcEntropy(datalist) == 0) {
					newNode.leaf = true;
					newNode.label = datalist.get(0).y;
					return newNode;
				}
				else {
					newNode = findBestSplit(datalist);
				}
			}
			else {
				newNode.leaf = true;
				newNode.label = findMajority(datalist);
			}
			return newNode;

			//return this; //dummy code.  Update while completing the assignment.
		}

		private DTNode findBestSplit(ArrayList<Datum> datalist) {
			//initializing the variables
			double bestAvgEntropy = 1;
			int bestAttribute = -1;
			double bestThreshold = -1;

			ArrayList<Datum> bestLeftList = new ArrayList<Datum>();
			ArrayList<Datum> bestRightList = new ArrayList<Datum>();

			DTNode curNode = new DTNode();

			for(int i = 0; i < datalist.get(0).x.length; i++) { //for each attribute
				for(int j = 0; j < datalist.size(); j++) { //for each data point in list
					ArrayList<Datum> leftList = new ArrayList<Datum>();
					ArrayList<Datum> rightList = new ArrayList<Datum>();
					double tmpThreshold = datalist.get(j).x[i];

					for(Datum datum : datalist) {
						if(datum.x[i] >= tmpThreshold) {
							rightList.add(datum);
						}
						else {
							leftList.add(datum);
						}
					}
					double leftListSize = leftList.size();
					double rightListSize = rightList.size();
					double totalSize = datalist.size();
					double leftEntropy = calcEntropy(leftList);
					double rightEntropy = calcEntropy(rightList);
					double currentAvgEntropy = ((rightListSize/totalSize) * rightEntropy) + ((leftListSize/totalSize) * leftEntropy);

					if(bestAvgEntropy > currentAvgEntropy) {
						bestAvgEntropy = currentAvgEntropy;
						bestAttribute = i;
						bestThreshold = tmpThreshold;
						bestRightList = rightList;
						bestLeftList = leftList;
					}
				}
			}
			if(bestAttribute == -1) {
				curNode.leaf = true;
				curNode.label = findMajority(datalist);
			}
			else {
				curNode.attribute = bestAttribute;
				curNode.threshold = bestThreshold;
				//System.out.println("attribute: " + bestAttribute + " threshold: " + bestThreshold + " bestAvgEntropy: " +  bestAvgEntropy);
				//System.out.println("Right");
				curNode.right = fillDTNode(bestRightList);
				//System.out.println("Left");
				curNode.left = fillDTNode(bestLeftList);
				curNode.leaf = false;
			}
			return curNode;
		}



		// This is a helper method. Given a datalist, this method returns the label that has the most
		// occurrences. In case of a tie it returns the label with the smallest value (numerically) involved in the tie.
		int findMajority(ArrayList<Datum> datalist) {

			int [] votes = new int[2];

			//loop through the data and count the occurrences of datapoints of each label
			for (Datum data : datalist)
			{
				votes[data.y]+=1;
			}

			if (votes[0] >= votes[1])
				return 0;
			else
				return 1;
		}




		// This method takes in a datapoint (excluding the label) in the form of an array of type double (Datum.x) and
		// returns its corresponding label, as determined by the decision tree
		int classifyAtNode(double[] xQuery) {
			//ADD CODE HERE
			if(this.leaf) {
				return this.label;
			}
			else {
				if(xQuery[this.attribute] >= this.threshold) {
					return this.right.classifyAtNode(xQuery);
				}
				return this.left.classifyAtNode(xQuery);
			}

			//return -1; //dummy code.  Update while completing the assignment.
		}


		//given another DTNode object, this method checks if the tree rooted at the calling DTNode is equal to the tree rooted
		//at DTNode object passed as the parameter
		public boolean equals(Object dt2)
		{
			//ADD CODE HERE
			DTNode DT2;
			try {
				if(dt2 == null) {
					return false;
				}
				DT2 = (DTNode) dt2;
			}
			catch (ClassCastException e) {
				return false;
			}
			/*System.out.println("Leaf: " + this.leaf  + " vs " + DT2.leaf);
			System.out.println("Attribute: " + this.attribute  + " vs " + DT2.attribute);
			System.out.println("Threshold: " + this.threshold  + " vs " + DT2.threshold);
			System.out.println("Label: " + this.label  + " vs " + DT2.label);*/
			if(!this.leaf && !DT2.leaf) { //both internal node
				if (this.threshold != DT2.threshold || this.attribute != DT2.attribute) { //check threshold & attribute
					return false;
				}
				//check if left and right are also the same
				return (this.left.equals(DT2.left) && this.right.equals(DT2.right));
			}
			else if(this.leaf && DT2.leaf) { //both leaf
				if(this.label != DT2.label) { //check label
					return false;
				}
				return true;
			}
			else { //their leaf value is not the same
				return false;
			}
			//return false; //dummy code.  Update while completing the assignment.
		}
	}



	//Given a dataset, this returns the entropy of the dataset
	double calcEntropy(ArrayList<Datum> datalist) {
		double entropy = 0;
		double px = 0;
		float [] counter= new float[2];
		if (datalist.size()==0)
			return 0;
		double num0 = 0.00000001,num1 = 0.000000001;

		//calculates the number of points belonging to each of the labels
		for (Datum d : datalist)
		{
			counter[d.y]+=1;
		}
		//calculates the entropy using the formula specified in the document
		for (int i = 0 ; i< counter.length ; i++)
		{
			if (counter[i]>0)
			{
				px = counter[i]/datalist.size();
				entropy -= (px*Math.log(px)/Math.log(2));
			}
		}

		return entropy;
	}


	// given a datapoint (without the label) calls the DTNode.classifyAtNode() on the rootnode of the calling DecisionTree object
	int classify(double[] xQuery ) {
		return this.rootDTNode.classifyAtNode( xQuery );
	}

	// Checks the performance of a DecisionTree on a dataset
	// This method is provided in case you would like to compare your
	// results with the reference values provided in the PDF in the Data
	// section of the PDF
	String checkPerformance( ArrayList<Datum> datalist) {
		DecimalFormat df = new DecimalFormat("0.000");
		float total = datalist.size();
		float count = 0;

		for (int s = 0 ; s < datalist.size() ; s++) {
			double[] x = datalist.get(s).x;
			int result = datalist.get(s).y;
			if (classify(x) != result) {
				count = count + 1;
			}
		}

		return df.format((count/total));
	}


	//Given two DecisionTree objects, this method checks if both the trees are equal by
	//calling onto the DTNode.equals() method
	public static boolean equals(DecisionTree dt1, DecisionTree dt2)
	{
		boolean flag = true;
		flag = dt1.rootDTNode.equals(dt2.rootDTNode);
		return flag;
	}

}