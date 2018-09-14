//JavaFX imports.
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Screen;

//Other Java imports.
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Optional;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.*;
import java.io.File;

public class RandomCropperGUI extends Application
{
	//Instance variables.
        static String selectedDirectoryUsable = null;
        static boolean JPG = false;
        static boolean jpg = false;
        static boolean PNG = false;
        static boolean png = false;
        static int numRandChunksTextFieldUsable;
        static int widthsizeRandChunksTextFieldUsable;
        static int heightsizeRandChunksTextFieldUsable;
        static boolean red = false;
        static boolean green = false;
        static boolean blue = false;
        static int thresholdValueTextFieldUsable;
        static int percentOfImageToMeetThresholdTextFieldUsable;
	static String filename;

	public static void main(String[] args)
	{
		launch(args);
	} 

	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage)
	{
		primaryStage.setTitle("RandomCropper V1.0");
	
		//Creating a GridPane container.
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setPadding(new Insets(25 , 25 , 25 , 25));
		grid.setVgap(10);
		grid.setHgap(10);	

		//Create Manual.		
		Button manualBtn = new Button();
		GridPane.setHalignment(manualBtn , HPos.RIGHT);
		manualBtn.setText("Manual (V1.0)");
		grid.add(manualBtn , 0 , 0 , 3 , 1);
		manualBtn.setOnAction(new EventHandler<ActionEvent>() 
		{	
			@Override
			public void handle(ActionEvent event)
			{
				Text scenetitlemanual = new Text("Manual V1.0");
				scenetitlemanual.setFont(Font.font("Courier New" , FontWeight.NORMAL , 20));

				//Create a StackPane container.
				StackPane panemanual = new StackPane();
				panemanual.getChildren().add(scenetitlemanual);

				Scene manualScene = new Scene(panemanual);
				Stage manualStage = new Stage();
				manualStage.setTitle("RandomCropper V1.0:  Manual");
				manualStage.setScene(manualScene);
				manualStage.show();
			}
		});

		//Create Scenetitle.
		Text scenetitle = new Text("RandomCropper V1.0");
		scenetitle.setFont(Font.font("Courier New" , FontWeight.NORMAL , 20));
		GridPane.setHalignment(scenetitle , HPos.CENTER);
		grid.add(scenetitle , 0 , 0 , 3 , 1);

		//Create Directory label, and button.
		Label directoryPath = new Label("Directory Path:");
		directoryPath.setFont(Font.font("Courier New" , FontWeight.NORMAL , 10));
		grid.add(directoryPath , 0 , 1);
		Button directoryBtn = new Button();
		GridPane.setHalignment(directoryBtn , HPos.LEFT);
		directoryBtn.setText("Open an Image Directory...");
		grid.add(directoryBtn , 1 , 1 , 3 , 1);
		ObjectProperty<File> inputFile = new SimpleObjectProperty<>();
		directoryBtn.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				final DirectoryChooser directoryChooser = new DirectoryChooser();
				final File selectedDirectory = directoryChooser.showDialog(primaryStage);
				if(selectedDirectory != null)
				{
					selectedDirectory.getAbsolutePath();
					inputFile.set(selectedDirectory);
					selectedDirectoryUsable = selectedDirectory.getAbsolutePath();
				}
			}
		});

		//Create File Type label and Checkboxes.
		Label fileType = new Label("Image File Type:");
                fileType.setFont(Font.font("Courier New" , FontWeight.NORMAL , 10));
                grid.add(fileType , 0 , 2);
		
		CheckBox JPGCheckbox = new CheckBox("JPG");
		CheckBox jpgCheckbox = new CheckBox("jpg");
		CheckBox PNGCheckbox = new CheckBox("PNG");
		CheckBox pngCheckbox = new CheckBox("png");
		HBox checkboxHBox = new HBox(10);
		checkboxHBox.getChildren().addAll(JPGCheckbox , jpgCheckbox , PNGCheckbox , pngCheckbox);
		grid.add(checkboxHBox , 1 , 2);
		
		//Create Checkbox Listeners to ensure only one of these checkboxes are clicked at a time.
		JPGCheckbox.selectedProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable , Boolean oldValue , Boolean newValue)
			{
				jpgCheckbox.setSelected(false);
				PNGCheckbox.setSelected(false);
				pngCheckbox.setSelected(false);	
				JPG = true;
				jpg = false;
				PNG = false;
				png = false;
			}	
		});
		
		jpgCheckbox.selectedProperty().addListener(new ChangeListener<Boolean>()
                {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable , Boolean oldValue , Boolean newValue)
                        {
                                JPGCheckbox.setSelected(false);
                                PNGCheckbox.setSelected(false);
                                pngCheckbox.setSelected(false);
				jpg = true;
				JPG = false;
				PNG = false;
				png = false;
                        }
                });

		PNGCheckbox.selectedProperty().addListener(new ChangeListener<Boolean>()
                {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable , Boolean oldValue , Boolean newValue)
                        {
                                JPGCheckbox.setSelected(false);
                                jpgCheckbox.setSelected(false);
                                pngCheckbox.setSelected(false);
				PNG = true;
				JPG = false;
				jpg = false;
				png = false;
                        }
                });

		pngCheckbox.selectedProperty().addListener(new ChangeListener<Boolean>()
                {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable , Boolean oldValue , Boolean newValue)
                        {
                                JPGCheckbox.setSelected(false);
                                jpgCheckbox.setSelected(false);
                                PNGCheckbox.setSelected(false);
				png = true;
				JPG = false;
				jpg = false;
				PNG = false;
                        }
                });

		//Create Number of Random Chunks Label and Text Field.
		Label numRandChunks = new Label("Number of Random Image Chunks (INT):");
                numRandChunks.setFont(Font.font("Courier New" , FontWeight.NORMAL , 10));
                grid.add(numRandChunks , 0 , 3);
                TextField numRandChunksTextField = new TextField();
                grid.add(numRandChunksTextField , 1 , 3);		
		
		//Add restrictions to Text Field to ensure only integers can be input.
		numRandChunksTextField.textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
                	{       
                        	if (!newValue.matches("\\d*")) 
                        	{
                                	numRandChunksTextField.setText(newValue.replaceAll("[^\\d]", ""));
                        	}
			}
                });

		//Create Size of Random Chunks Label and Text Field.
		Label sizeRandChunks = new Label("Dimensions of the Random Image Chunks (WxH):");
                sizeRandChunks.setFont(Font.font("Courier New" , FontWeight.NORMAL , 10));
                grid.add(sizeRandChunks , 0 , 4);
                TextField widthsizeRandChunksTextField = new TextField();
                grid.add(widthsizeRandChunksTextField , 1 , 4);
		
		//Add restrictions to Text Field to ensure only integers can be input.
		widthsizeRandChunksTextField.textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
                	{       
                        	if (!newValue.matches("\\d*")) 
                        	{
                                	widthsizeRandChunksTextField.setText(newValue.replaceAll("[^\\d]", ""));
                        	}
			}
                });

		TextField heightsizeRandChunksTextField= new TextField();
		grid.add(heightsizeRandChunksTextField , 2 , 4);
		
		heightsizeRandChunksTextField.textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
                	{       
                        	if (!newValue.matches("\\d*")) 
                        	{
                                	heightsizeRandChunksTextField.setText(newValue.replaceAll("[^\\d]", ""));
                        	}
			}
                });

		//Create Threshold Channel Label and Checkboxes.
		Label thresholdChannel = new Label("Thresholding Channel (RGB):");
                thresholdChannel.setFont(Font.font("Courier New" , FontWeight.NORMAL , 10));
                grid.add(thresholdChannel , 0 , 5);
		CheckBox RCheckbox = new CheckBox("R");
                CheckBox GCheckbox = new CheckBox("G");
                CheckBox BCheckbox = new CheckBox("B");
                HBox RGBcheckboxHBox = new HBox(10);
                RGBcheckboxHBox.getChildren().addAll(RCheckbox , GCheckbox , BCheckbox);
                grid.add(RGBcheckboxHBox , 1 , 5);

		//Add listener to ensure only one of these checkboxes are clicked at a time.
                RCheckbox.selectedProperty().addListener(new ChangeListener<Boolean>()
                {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable , Boolean oldValue , Boolean newValue)
                        {
                                GCheckbox.setSelected(false);
                                BCheckbox.setSelected(false);
				if(newValue)
				{
					red = true;
					green = false;
					blue = false;
				}				
			}
                });

                GCheckbox.selectedProperty().addListener(new ChangeListener<Boolean>()
                {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable , Boolean oldValue , Boolean newValue)
                        {
                                RCheckbox.setSelected(false);
                                BCheckbox.setSelected(false);
				if(newValue)
				{
					green = true;
					red = false;
					blue = false;
				}
                        }
                });

                BCheckbox.selectedProperty().addListener(new ChangeListener<Boolean>()
                {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable , Boolean oldValue , Boolean newValue)
                        {
                                RCheckbox.setSelected(false);
                                GCheckbox.setSelected(false);
				if(newValue)
				{
					blue = true;
					red = false;
					green = false;
				}
                        }
             	});

		//Create Threshold Value label and Text Field.
		Label thresholdValue = new Label("Thresholding Value (INT):");
                thresholdValue.setFont(Font.font("Courier New" , FontWeight.NORMAL , 10));
                grid.add(thresholdValue , 0 , 6);
                TextField thresholdValueTextField = new TextField();
                grid.add(thresholdValueTextField , 1 , 6);

		//Add listener to ensure only integers can be input.
		thresholdValueTextField.textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
                	{       
                        	if (!newValue.matches("\\d*")) 
                        	{
                                	thresholdValueTextField.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
                });

		//Create Percent of Image to Meet Threshold Label and Text Field
		Label percentOfImageToMeetThreshold = new Label("Percent of Random Image Chunk that needs to meet Thresholding Value (INT):");
		percentOfImageToMeetThreshold.setFont(Font.font("Courier New" , FontWeight.NORMAL , 10));
                grid.add(percentOfImageToMeetThreshold , 0 , 7);
                TextField percentOfImageToMeetThresholdTextField = new TextField();
                grid.add(percentOfImageToMeetThresholdTextField , 1 , 7);

		//Add listener to ensure only integers can be input.
		percentOfImageToMeetThresholdTextField.textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
                	{       
                        	if (!newValue.matches("\\d*")) 
                        	{
                                	percentOfImageToMeetThresholdTextField.setText(newValue.replaceAll("[^\\d]", ""));
                        	}
			}
                });

		//Button checks.
		//Ensure all text fields are filled.
		BooleanBinding createChunksbbtext = new BooleanBinding()
		{
			{
				super.bind(numRandChunksTextField.textProperty() , widthsizeRandChunksTextField.textProperty() , heightsizeRandChunksTextField.textProperty()  ,  thresholdValueTextField.textProperty() , percentOfImageToMeetThresholdTextField.textProperty());
			}
			
			@Override
			protected boolean computeValue()
			{
				return(numRandChunksTextField.getText().isEmpty() ||  widthsizeRandChunksTextField.getText().isEmpty() || heightsizeRandChunksTextField.getText().isEmpty() || thresholdValueTextField.getText().isEmpty() || percentOfImageToMeetThresholdTextField.getText().isEmpty());
			}
		};		

		//Ensure Directory has been selected.
		BooleanBinding fileExists = Bindings.createBooleanBinding(() -> inputFile.get() != null && inputFile.get().exists() , inputFile);
		//

		//Get size of screen.
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

		//Add Run button.
		Button createChunks = new Button("Create Random Chunks");
		
		//IMPORTANT TO ENSURE PROPER createChunks BUTTON ACTIVATION.
		createChunks.disableProperty().bind(createChunksbbtext.or((JPGCheckbox.selectedProperty().not()).and(jpgCheckbox.selectedProperty().not()).and(PNGCheckbox.selectedProperty().not()).and(pngCheckbox.selectedProperty().not())).or(RCheckbox.selectedProperty().not().and(GCheckbox.selectedProperty().not()).and(BCheckbox.selectedProperty().not())).or(fileExists.not()));
		//Add the createChunks button to grid.
		GridPane.setHalignment(createChunks , HPos.CENTER);
		grid.add(createChunks , 0 , 10 , 3 , 1);

		//Add Fatal error 1 error message text.
                final Text errortargetone = new Text();
                GridPane.setHalignment(errortargetone , HPos.CENTER);
                errortargetone.setFill(Color.FIREBRICK);
		grid.add(errortargetone , 0 , 12 , 4 , 1);
                //errortargetone.setText("Fatal Error 1: The dimensions of the random chunks requested either exceed the dimensions or are less than or equal to zero for " + test.filename + ".");

		//Add Fatal error 2 error message text.
                final Text errortargettwo = new Text();
                GridPane.setHalignment(errortargettwo , HPos.CENTER);
                errortargettwo.setFill(Color.FIREBRICK);
		grid.add(errortargettwo , 0 , 13 , 4 , 1);
                //errortargettwo.setText("Fatal Error 2: The amount of random chunks requested exceeds that allowed by the size of " + test.filename + " within " + selectedDirectoryUsable + ".");

		//Add Fatal error 3 error message text.
		final Text errortargetthree = new Text();
		GridPane.setHalignment(errortargetthree , HPos.CENTER);
		errortargetthree.setFill(Color.FIREBRICK);
		grid.add(errortargetthree , 0 , 13 , 4 , 1);

		//Run RandomCropper Code when Button is pressed.
		createChunks.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				//Capture user input.
				numRandChunksTextFieldUsable = Integer.parseInt(numRandChunksTextField.getText());
				widthsizeRandChunksTextFieldUsable = Integer.parseInt(widthsizeRandChunksTextField.getText());
				heightsizeRandChunksTextFieldUsable = Integer.parseInt(heightsizeRandChunksTextField.getText());
				thresholdValueTextFieldUsable = Integer.parseInt(thresholdValueTextField.getText());
				percentOfImageToMeetThresholdTextFieldUsable = Integer.parseInt(percentOfImageToMeetThresholdTextField.getText());
				
				//Start RandomCropperBackend.
				try
				{	
					RandomCropperBackend.main(null);
					if(RandomCropperBackend.errortargetonebool && RandomCropperBackend.errortargettwobool && RandomCropperBackend.errortargetthreebool)
					{
						//Display Fatal 1, 2, and 3 error messages. 
						errortargetone.setText("Fatal Error 1: The dimensions of the random chunks requested either exceed the dimensions or are less than or equal to zero for " + RandomCropperBackend.filename + ".");
						errortargettwo.setText("Fatal Error 2: The amount of random chunks requested exceeds that allowed by the size of " + RandomCropperBackend.filename + " within " + selectedDirectoryUsable + ".");
						errortargetthree.setText("Fatal Error 3: You have requested a percentage to meet threshold that is less than zero or greater than 100.");
						primaryStage.sizeToScene();

						//Close the Stage.
						primaryStage.setOnCloseRequest(e -> Platform.exit());
					}

					if(RandomCropperBackend.errortargetonebool && RandomCropperBackend.errortargettwobool)
					{
						//Display Fatal 1 and 2 error messages.
						errortargetone.setText("Fatal Error 1: The dimensions of the random chunks requested either exceed the dimensions or are less than or equal to zero for " + RandomCropperBackend.filename + ".");
                                                errortargettwo.setText("Fatal Error 2: The amount of random chunks requested exceeds that allowed by the size of " + RandomCropperBackend.filename + " within " + selectedDirectoryUsable + ".");
						primaryStage.sizeToScene();						

						//Close the Stage.
                                                primaryStage.setOnCloseRequest(e -> Platform.exit());
					}

					if(RandomCropperBackend.errortargetonebool && RandomCropperBackend.errortargetthreebool)
					{
						errortargetone.setText("Fatal Error 1: The dimensions of the random chunks requested either exceed the dimensions or are less than or equal to zero for " + RandomCropperBackend.filename + ".");
						errortargetthree.setText("Fatal Error 3: You have requested a percentage to meet threshold that is less than zero or greater than 100.");
                                                primaryStage.sizeToScene();

						//Close the Stage.
                                                primaryStage.setOnCloseRequest(e -> Platform.exit());
					}

					if(RandomCropperBackend.errortargettwobool && RandomCropperBackend.errortargetthreebool)
					{
						errortargettwo.setText("Fatal Error 2: The amount of random chunks requested exceeds that allowed by the size of " + RandomCropperBackend.filename + " within " + selectedDirectoryUsable + ".");
						errortargetthree.setText("Fatal Error 3: You have requested a percentage to meet threshold that is less than zero or greater than 100.");
                                                primaryStage.sizeToScene();

						//Close the Stage.
                                                primaryStage.setOnCloseRequest(e -> Platform.exit());
					}

					if(RandomCropperBackend.errortargetonebool)
                                	{
                                        	//Display Fatal error 1 error message text.
                                        	errortargetone.setText("Fatal Error 1: The dimensions of the random chunks requested either exceed the dimensions or are less than or equal to zero for " + RandomCropperBackend.filename + ".");
                                        	primaryStage.sizeToScene();

						//Close the Stage.
						primaryStage.setOnCloseRequest(e -> Platform.exit());
                                	}

                                	if(RandomCropperBackend.errortargettwobool) 
					{
                                        	//Display Fatal error 2 error message text.
                                        	errortargettwo.setText("Fatal Error 2: The amount of random chunks requested exceeds that allowed by the size of " + RandomCropperBackend.filename + " within " + selectedDirectoryUsable + ".");
						primaryStage.sizeToScene();

						//Close the stage.
						primaryStage.setOnCloseRequest(e -> Platform.exit());
                                	}

					if(RandomCropperBackend.errortargetthreebool)
                                        {
                                                //Display Fatal error 3 error message text.
                                                errortargetthree.setText("Fatal Error 3: You have requested a percentage to meet threshold that is less than zero or greater than 100.");
                                                primaryStage.sizeToScene();

                                                //Close the stage.
                                                primaryStage.setOnCloseRequest(e -> Platform.exit());
                                        }
				}

				catch (IOException e)
				{
					e.printStackTrace();
				}
			
				//Create alert.
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
				alert.setTitle("RandomCropper V1.0");
				alert.setHeaderText("Post Run Confirmation");
				alert.setContentText("Run RandomCropper V1.0 on another directory?");
				ButtonType neww = new ButtonType("New");
				ButtonType exit = new ButtonType("Exit");
				alert.getButtonTypes().setAll(neww , exit);
				Optional<ButtonType> result = alert.showAndWait();
				
				//If neww is selected, clear the original stage and close dialog.
                                if(result.get() == neww)
                                {
                                        //Clear input.
                                        numRandChunksTextField.clear();
                                        widthsizeRandChunksTextField.clear();
                                        heightsizeRandChunksTextField.clear();
                                        thresholdValueTextField.clear();
                                        percentOfImageToMeetThresholdTextField.clear();
                                        JPGCheckbox.setSelected(false);
                                        jpgCheckbox.setSelected(false);
                                        PNGCheckbox.setSelected(false);
                                        pngCheckbox.setSelected(false);
                                        RCheckbox.setSelected(false);
                                        GCheckbox.setSelected(false);
                                        BCheckbox.setSelected(false);

					//Hide the Fatal Error message if one or more exists.
					if(RandomCropperBackend.errortargetonebool && RandomCropperBackend.errortargettwobool && RandomCropperBackend.errortargetthreebool)
					{
						errortargetone.setText("");
						errortargettwo.setText("");
						errortargetthree.setText("");
					}

					if(RandomCropperBackend.errortargetonebool && RandomCropperBackend.errortargettwobool)
					{
						errortargetone.setText("");
                                                errortargettwo.setText("");
					}
			
					if(RandomCropperBackend.errortargetonebool && RandomCropperBackend.errortargetthreebool)
					{
						errortargetone.setText("");
						errortargetthree.setText("");
					}

					if(RandomCropperBackend.errortargettwobool && RandomCropperBackend.errortargetthreebool)
					{
						errortargettwo.setText("");
                                                errortargetthree.setText("");
					}
					
					if(RandomCropperBackend.errortargetonebool)
					{
						errortargetone.setText("");
					}

					if(RandomCropperBackend.errortargettwobool)
					{
						errortargettwo.setText("");
					}

                                        //Close dialog.
                                        alert.close();
                                }

                                //If exit is selected, close the primaryStage and close dialog.
                                if(result.get() == exit)
                                {
                                        primaryStage.close();
                                        alert.close();
                                }
			}					
		});

		Scene scene = new Scene(grid);
		primaryStage.setScene(scene);
		primaryStage.show();
	}	
}
