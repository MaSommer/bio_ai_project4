package jssp;

public class VariablesPSO {
	
	//PSO Variables
	public static int swormSize = 30;
	public static double minPosition = 0;
	public static double maxPosition = 2;
	public static double minVelocity = -2;
	public static double maxVelocity = 2;
	
	public static double c1 = 2.0;
	public static double c2 = 2.0;
	
	public static double initialOmega = 1.4;
	public static double terminalOmega = 0.4;
	public static double iterationsUntilOmegaTerminal = 1000;
	
	public static double probSwappingRate = 0.4;
	public static double probInsertionRate = 0.4;
	public static double probInversionRate = 0.1;
	public static double probLongDistanceMovementRate = 0.1;
	public static double multiTypeIndividualEnhancementSchemeRate = 0.01;
	
	public static double finalTemperature = 0.1;
	public static double beta = 0.97;
	
	

}
