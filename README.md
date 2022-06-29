# Clipboard to Txt Complier

Clipboard to Txt complier is a Java program that helps to compile user copied content into text files. It listens to clipboard changes and temporarily stores them together before user save it in a .txt file. Comes with in an in-built file browser and file viewer section to help manage and view saved text files.

## Usage

### 1. Choose output folder
Insert the desired output folder where your text files will be saved to in the `Output Folder` text field. You can also select `Choose folder` to do so. When a valid directory is selected, the contents of the folder will be shown in the file browser area. 

### 2. Indicate file name
Insert the desired file name in the `File Name` text field. 

For more control over the file name, you can use the `Leading`, `Number` and `Trailing` text fields.
- **Leading** - starting part of the final file name
- **Number** - the number to label the file name (Only **positive numbers** (no decimals) can be input in this field!)
- **Trailing** - ending part of the final file name

The contents of the three text fields will be combined (leading, number, trailing) together to form the file name in the `File Name` text field.

e.g. `Leading`: Hello | `Number`: 1 | `Trailing`: World | `File name`: Hello 1 World

When saving file, any of these 3 fields can be empty, but `File name` text field **cannot be empty**.

e.g. *Correct* `Leading`: Hello | `Number`: <empty> | `Trailing`: <empty> | `File name`: Hello

e.g. *Correct* `Leading`: <empty> | `Number`: <empty> | `Trailing`: <empty> | `File name`: Hello world

e.g. *Wrong* `Leading`: <empty> | `Number`: <empty> | `Trailing`: <empty> | `File name`: <empty>

### 3. Starting the complier
Click `Start` at the top left hand corner for the complier to start listening to clipboard changes and the status will change to "Tracking". You can toggle it again to stop tracking clipboard changes.


### 4. Copying content
Once tracking has started, you can navigate to other programs and start copying text. Every section of copied text will be shown in the `clipboard` section (Above `File Name` and `Save Manually` button). The text will be compiled and the accumulated text will be shown in the `Current Text File` section. 

> Note: The same text copied *multiple* times will **NOT** be accumulated to the current text file. Only the first instance will be recorded to prevent multiple accidental copy operations recorded. 

If you would like to record multiple instances of the same text, click `Duplicate clipboard` as many times as required. 

### 5. Saving file
Once the desired texts have been copied, you can click the `Save Manually` button or use Ctrl+S keyboard shortcut to save the accumulated text to the desired output folder and file name (in `Output Folder` and `File Name` text fields). 

> If file name already exists in current output folder, you will be asked to override existing file with the new content. 

If `Increment number after save` checkbox is checked, the value in `Number` text field will be incremented.

## Additional features

### Increment number after save
By checking this checkbox, the value in the `Number` text field will be incremented when a new text file is saved. In the case of recording text with incremental file names, it can be useful to check this option. 
If you would like to keep the existing number at any point, uncheck this field.

### File browser popup menu
More file operations can be accessed through the popup menu when right clicking files in the file browser. Operations include
1. **Open**
2. **Open with Notepad++ program**
3. **Rename** - If renamed file is an existing file, you can choose to override the existing file or abort the operation)
4. **Delete** - Will ask user to confirm delete operation

Note: These file operations can only be performed on **text** files only! For more functionality, please access the files in Windows file explorer by clicking `Show in Explorer`.

### File browser actions
At the top of file browser section, there are four buttons to control file browser:
1. **Back** - Navigate to parent directory of the current folder
2. **Refresh** - Update file browser with any changes (usually required if changes are made externally in Windows file explorer)
3. **Sort Name** - Sort files by name in ascending or descending order (Indicated with arrow)
4. **Sort Date** - Sort files by last modified date in ascending or descending order (Indicated with arrow)

For sorting, folders will be shown on top for ascending order while files will be shown on top for descending order

### Pad zeroes
Enter the minimum number of digits you want the number value to display. This feature will include leading zeroes in this number if the length of the number is lesser than the specified length.

E.g. Pad zeroes **less than** length of number
- `Pad zeroes`: 3 | `Number`: 45 | `File Name`: 045
- `Pad zeroes`: 5 | `Number`: 123 | `File Name`: 00123

E.g. Pad zeroes **more than or equal** to length of number
- `Pad zeroes`: 2 | `Number`: 45 | `File Name`: 45
- `Pad zeroes`: 2 | `Number`: 145 | `File Name`: 145
- `Pad zeroes`: 3 | `Number`: 12345 | `File Name`: 12345