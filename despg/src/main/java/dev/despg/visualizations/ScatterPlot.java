package dev.despg.visualizations;

import java.util.HashMap;

import dev.despg.core.Randomizer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ScatterPlot extends Application
{
	private static final String PLOT_TITLE = "Triangular Density";
	private static final int PIXEL_Y = 800;
	private static final int PIXEL_X = 1000;

	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception
	{
		stage.setTitle(PLOT_TITLE);

		Randomizer r = new Randomizer();
		Series<Number, Number> xy = new Series<Number, Number>();
		double min = 1;
		double mode = 8;
		double max = 10;
		double precision = 0.1;
		final int numbersComputed = 10000;

		HashMap<Double, Double> densityFunction = new HashMap<Double, Double>();

		for (int i = 0; i < numbersComputed; i++)
		{
			double value = Math.round(r.getTriangular(min, max, mode) / precision) * precision;
			Double existingValue = densityFunction.get(value);

			densityFunction.put(value, existingValue == null ? 1.0 : ++existingValue);
		}

		for (Double key : densityFunction.keySet())
			densityFunction.put(key, densityFunction.get(key) / numbersComputed);

		for (Double key : densityFunction.keySet())
			xy.getData().add(new XYChart.Data<Number, Number>(key, densityFunction.get(key)));

		NumberAxis xAxis = new NumberAxis(min, max, 0.01);
		NumberAxis yAxis = new NumberAxis();
		ScatterChart<Number, Number> sc = new ScatterChart<Number, Number>(xAxis, yAxis);
		xAxis.setLabel("random value");
		yAxis.setLabel("probability");

		sc.getData().add(xy);
		sc.setPrefSize(PIXEL_X, PIXEL_Y);
		sc.setTitle(PLOT_TITLE);

		VBox vb = new VBox();
		vb.getChildren().add(sc);
		stage.setScene(new Scene(vb, PIXEL_X, PIXEL_Y));
		stage.show();
	}
}

