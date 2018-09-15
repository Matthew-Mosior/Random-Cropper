//General Java imports.
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.*;
import java.io.File;

public class RandomCropperBackend
{
    static boolean errortargetonebool;
    static boolean errortargettwobool;
    static boolean errortargetthreebool;
    static String filename;

    public static void main(String[] args) throws IOException
    {
	//General Use Variables.
	String last;
	String lastnew;
	String lastfinal;

        File folder = new File(RandomCropperGUI.selectedDirectoryUsable);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) 
        {
            if (file.isFile()) 
            {
		//Reset Error Target Booleans.
		errortargetonebool = false;
		errortargettwobool = false;
		errortargetthreebool = false;
		//

		//Check image file type.
		if(RandomCropperGUI.JPG)
		{
                	last = file.getName();
                	lastnew = last.replace(".JPG" , "");
                	lastfinal = "/" + lastnew;
		}

		else if(RandomCropperGUI.jpg)
		{
			last = file.getName();
		        lastnew = last.replace(".jpg" , "");
			lastfinal = "/" + lastnew;				
		}

		else if(RandomCropperGUI.PNG)
		{
			last = file.getName();
		      	lastnew = last.replace(".PNG" , "");
			lastfinal = "/" + lastnew;	
		}

		else
		{
			last = file.getName();
			lastnew = last.replace(".png" , "");
			lastfinal = "/" + lastnew;
		}
		//

                //Create FileInputStream for new image file.              
                FileInputStream fis = new FileInputStream(file); 
                BufferedImage image = ImageIO.read(fis); //reading the image file.
               
		//Calculate finalfinalintialcrop width and height.
		int true_width = image.getWidth();
		int true_height = image.getHeight();

		//Perform Sanity Checks.
		//Ensure that requested width and height of random image chunks don't exceed size of image dimensions, and are greater than zero.
		if((RandomCropperGUI.widthsizeRandChunksTextFieldUsable >= true_width) || (RandomCropperGUI.heightsizeRandChunksTextFieldUsable >= true_height) || (RandomCropperGUI.widthsizeRandChunksTextFieldUsable <= 0) || (RandomCropperGUI.heightsizeRandChunksTextFieldUsable <= 0))
		{
			errortargetonebool = true;
			filename = file.getName();
		}

		//Calculate "adjusted" width and height based on chunk sizes requested random chunk dimensions.
		int adjusted_width = true_width - RandomCropperGUI.widthsizeRandChunksTextFieldUsable;
		int adjusted_height = true_height - RandomCropperGUI.heightsizeRandChunksTextFieldUsable;
		
		//Perform Sanity Checks.
		//Use these adjusted dimensions to ensure the user is not requesting too many random chunks based on image size (only some many possible combinations of (x,y)).
		if((adjusted_width * adjusted_height) < RandomCropperGUI.numRandChunksTextFieldUsable)
		{
			errortargettwobool = true;
			filename = file.getName();
		}

		//Perform Sanity Check.
		//Check to ensure that percent of image to meet threshold value isnt less than zero or greater than 100.
		if(RandomCropperGUI.percentOfImageToMeetThresholdTextFieldUsable < 0 || RandomCropperGUI.percentOfImageToMeetThresholdTextFieldUsable > 100)
		{
			errortargetthreebool = true;
			filename = file.getName();
		}

		//Return if either one of the two sanity checks fail.
		if(errortargetonebool || errortargettwobool || errortargetthreebool)
		{
			return;
		}
 		//

		//Add make a new directory based off current file name).
                new File(RandomCropperGUI.selectedDirectoryUsable + lastfinal).mkdir();

                //Variables for writing image chunks to files.
                String pathout = RandomCropperGUI.selectedDirectoryUsable + lastfinal + "/";
                File outputfile = new File(pathout);
                outputfile.getParentFile().mkdirs();
                File parentDir = outputfile.getParentFile();

                if(parentDir != null && ! parentDir.exists())
                {
                        if(!parentDir.mkdirs())
                        {
                                throw new IOException("Error creating directories.");
                        }
                }

		//Random class for rand_jump calculation and other variables.
		Random randh = new Random(System.currentTimeMillis());
		Random randv = new Random(System.currentTimeMillis());
		List<List<Integer>> xycoor = new ArrayList<List<Integer>>();
		List<List<Integer>> xycoor_check = new ArrayList<List<Integer>>();
		List<List<Integer>> xycoor_check_dup = new ArrayList<List<Integer>>();
		int number_of_rand_chunks = RandomCropperGUI.numRandChunksTextFieldUsable;
		int rand_chunk_counter = 0;
		int min_h_random_jump;
		int max_h_random_jump;
		int min_v_random_jump;
		int max_v_random_jump;
		int rand_jump_h;
		int rand_jump_v;
		int rand_jump_int_h;
		int rand_jump_int_v;
		int rand_chunkWidth;
		int rand_chunkHeight;
		int thresholdcounter;
		int checkcounter = 0;
		int iRed;
		int iGreen;
		int iBlue;

		while(rand_chunk_counter < number_of_rand_chunks)
		{
			//Calculate random horizontal jump.
			randh = new Random();
			min_h_random_jump = 0;
			max_h_random_jump = true_width - RandomCropperGUI.widthsizeRandChunksTextFieldUsable;
			rand_jump_h = randh.nextInt((max_h_random_jump - min_h_random_jump) + 1) + min_h_random_jump;
			
			//Calculate random vertical jump.
			randv = new Random();
			min_v_random_jump = 0;
			max_v_random_jump = true_height - RandomCropperGUI.heightsizeRandChunksTextFieldUsable;
			rand_jump_v = randv.nextInt((max_v_random_jump - min_v_random_jump) + 1) + min_v_random_jump;

			xycoor_check.add(Arrays.asList(rand_jump_h , rand_jump_v));

			//Ensure that the new random jump pair hasn't already been used.
			//Check to see if xycoor_check is empty.
			if(!xycoor.isEmpty())
			{
				for(int i = 0 ; i < xycoor_check.size() ; i++)
				{
					//If not empty and it does contain current random jump pair, then rerun horizontal and vertical random jump calculations.
					if(xycoor.contains(xycoor_check.get(i)))
					{
						do
						{	
							//Clear previous check.
							xycoor_check_dup.clear();
							
							//Calculate random horizontal jump (make sure it is not a multiple of 64 because we want unique image chunks).
                        				randh = new Random();
                       	 				min_h_random_jump = 0;
                        				max_h_random_jump = true_width - RandomCropperGUI.widthsizeRandChunksTextFieldUsable;
                        				rand_jump_h = randh.nextInt((max_h_random_jump - min_h_random_jump) + 1) + min_h_random_jump;

                        				//Calculate random vertical jump (make sure it is not a multiple of 64 because we want unique image chunks).
                        				randv = new Random();
                        				min_v_random_jump = 0;
                        				max_v_random_jump = true_height - RandomCropperGUI.heightsizeRandChunksTextFieldUsable;
                        				rand_jump_v = randv.nextInt((max_v_random_jump - min_v_random_jump) + 1) + min_v_random_jump;

							xycoor_check_dup.add(Arrays.asList(rand_jump_h , rand_jump_v));

						} while(xycoor_check_dup.equals(xycoor_check));
						
						//Add the new, unique horizontal and vertical random jump calcuations to the xycoor list.
						xycoor.add(Arrays.asList(rand_jump_h , rand_jump_v));
						checkcounter++;			
					}

					//If not empty and it doesn't contain current random jump pair, then add the unique horizontal and vertical random jump calculations to the xycoor list. 
					else
					{
						xycoor.add(Arrays.asList(rand_jump_h , rand_jump_v));	
					}
				}
			}

			//xycoor_check is empty, so add horizontal and vertical random jump calculations to the xycoor list.
			else
			{
				xycoor.add(Arrays.asList(rand_jump_h , rand_jump_v));
			}
	
			//Clear xycoor_check for next iteration.	
			xycoor_check.clear();
			
			//Type cast rand_jump_int_h and rand_jump_int_v.
			rand_jump_int_h = (int)rand_jump_h;
			rand_jump_int_v = (int)rand_jump_v;

			//Calculate chunk width and height.
			rand_chunkWidth = RandomCropperGUI.widthsizeRandChunksTextFieldUsable; //Determined chunk width (Input from user).
			rand_chunkHeight = RandomCropperGUI.heightsizeRandChunksTextFieldUsable; //Determined chunk height (Input from user).

			//Create new BufferedImage to hold current image chunk.
			BufferedImage image_chunk = new BufferedImage(rand_chunkWidth , rand_chunkHeight , image.getType());

			//Draw the current image chunk.
			Graphics2D gr = image_chunk.createGraphics();
			gr.drawImage(image , 0 , 0 , rand_chunkWidth , rand_chunkHeight , rand_jump_int_h , rand_jump_int_v , rand_jump_int_h + rand_chunkWidth , rand_jump_int_v + rand_chunkHeight, null);
			gr.dispose();

			//Counter for threshold testing.
			thresholdcounter = 0;

			//Thresholding channel ints.
			iRed = 0;
			iGreen = 0;
			iBlue = 0;
	
			//Check Thresholding Channel Specified By User.	
			if(RandomCropperGUI.red)
			{
				//Grab chunk if it meets Red channel threshold.
				for(int j = 0 ; j < image_chunk.getWidth() ; j++)
				{
					for(int k = 0 ; k < image_chunk.getHeight() ; k++)
					{
						iRed = new Color(image_chunk.getRGB(j , k)).getRed();

						//Increment counter if Red Channel Threshold meets or exceeds user specified requirement.
						if(iRed >= RandomCropperGUI.thresholdValueTextFieldUsable)
						{
							thresholdcounter++;
						}
					}
				}
			}

			if(RandomCropperGUI.green)
			{
				//Grab chunk if it meets Green channel threshold.
				for(int j = 0 ; j < image_chunk.getWidth() ; j++)
				{
					for(int k = 0 ; k < image_chunk.getHeight() ; k++)
					{
						iGreen = new Color(image_chunk.getRGB(j , k)).getGreen();

						//Increment counter if Green Channel Threshold meets or exceeds user specified requirement.
						if(iGreen >= RandomCropperGUI.thresholdValueTextFieldUsable)
						{
							thresholdcounter++;
						}
					}
				}
			}

			if(RandomCropperGUI.blue)
			{
				//Grab chunk if it meets Blue channel threshold.
				for(int j = 0 ; j < image_chunk.getWidth() ; j++)
				{
					for(int k = 0 ; k < image_chunk.getHeight() ; k++)
					{
						iBlue = new Color(image_chunk.getRGB(j , k)).getBlue();
						//Increment counter if Blue Channel Threshold meets or exceeds user specified requirement.
						if(iBlue >= RandomCropperGUI.thresholdValueTextFieldUsable)
						{
							thresholdcounter++;
						}
					}
				}
			}

			if(thresholdcounter > ((RandomCropperGUI.widthsizeRandChunksTextFieldUsable * RandomCropperGUI.heightsizeRandChunksTextFieldUsable) * (RandomCropperGUI.percentOfImageToMeetThresholdTextFieldUsable / 100)))
			{
				rand_chunk_counter++;
				
				if(RandomCropperGUI.JPG)
				{
					ImageIO.write(image_chunk , "JPG" , new File(pathout + "img_" + rand_chunk_counter + "_" + lastnew + ".JPG" ));
				}
				
				else if(RandomCropperGUI.jpg)
				{
					ImageIO.write(image_chunk , "jpg" , new File(pathout + "img_" + rand_chunk_counter + "_" + lastnew + ".jpg"));
				}
				
				else if(RandomCropperGUI.PNG)
				{
					ImageIO.write(image_chunk , "PNG" , new File(pathout + "img_" + rand_chunk_counter + "_" + lastnew + ".PNG"));
				}

				else
				{
					ImageIO.write(image_chunk , "png" , new File(pathout + "img_" + rand_chunk_counter + "_" + lastnew + ".png"));
				}
			}
		    }
              }
         }
    }
}
