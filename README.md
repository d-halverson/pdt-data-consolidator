# pdt-data-consolidator

## Problem Statement
The Data Consolidator project's purpose is to read lines of text from multiple files in a given directory, and output all the unique lines from these files into a single text file in sorted order.

## Assumptions About Input
1. All files in the inputted directory are newline-delimited text files, with each line in a sorted lexicographically.

## Usage
Assuming you have Java installed, follow these instructions to run this project ([download Java here if you need to](https://www.oracle.com/java/technologies/downloads/)).
1. Change working directory into the root of this project:
```
cd pdt-data-consolidator
```
2. Compile all java files:
```
javac -d bin src/main/java/com/pdt/dataconsolidator/*.java
```
3. Run the command line program (replace <input_dir> and <output_file> with the directory to read from, and the location to write to, respectively):
```
java -cp bin com.pdt.dataconsolidator.DataConsolidator <input_dir> <output_file>
```
Example run command:
```
java -cp bin com.pdt.dataconsolidator.DataConsolidator src/test/resources/given-test-case output.txt
```

## Approach 
When considering approaches to this problem, I decided to favor runtime complexity and readability of code over space complexity. I also decided to put a lower priority on how well it scales with the number of files in the input directory, because in many real world scenarios the number of files won't get extremely large where this would be a bottleneck. This led me to the following approach.
1. Initialize pointers (in this case BufferedReaders in java) to the beginning of each file in the inputted directory.
2. Take the smallest (lexicographically) current line of all the file pointers, and write that line to the output file if that line does not already exist in the output (use a hash set to track written lines in constant time).
3. Increment the file pointer that just had a line written to the output file.
4. Continue until all file pointers reach the end of file.

### Pros
- Fast runtime even in scenarios where there are many lines and many unique lines, because each line in each file only needs to be read once, and avoiding writing duplicates can be done in constant time with the hash set.
- Readable code that is easy to modify and debug later.

### Cons
- Stores all unique lines in memory in the hash set, so if the number of unique lines grows to be huge, it is possible to run into an out of memory exception.
- If the number of files grows very large, step 2 of the above approach may be a bottleneck, since it needs to compare all the current lines to find the smallest one. I chose to live with this downfall, because like mentioned previously, in real world scenarios often the number of files would not be extremely big enough for this to be an issue.

### Complexity of Approach
#### Runtime Complexity `O(k * n)`
Let `k` denote the number of files in the input directory, and `n` denote the number of total lines across all files.
Because there are `k` pointers to each file, each time the next smallest line is being written, `k` comparisons are needed. This is done `n` times, so the overall runtime complexity is `O(k * n)`. Checking if each line being written is unique across all files is done in `O(1)` because of the hash set, so it is not included in the overall runtime complexity.

#### Space Complexity `O(k + m)`
Let `k` denote the number of files in the input directory, and `m` denote the number of total unique lines across all files.
There are `k` pointers to each file, so we need `k` space to read all files simultaneously. We also need `m` space to store all the unique lines in the hash set. Because of this, the total space complexity is `O(k + m)`.

## Testing
The main testing is done in `DataConsolidatorTest`, where I have several test cases reading from test directories in the `resources` folder. I wrote these tests in a readable and modular way, so that new cases can be added by simply adding an item to the input and output arrays. A for loop then iterates over all the inputs and compares the output to the expected output.

Some examples of test cases I covered:
- The given test case from the assignment
- Invalid input directory
- Files with only empty lines
- Directory with only one file
- Multiple files
- Many lines
- Many lines with one of the files containing no unique lines
- Case sensitivity (APPLE and apple should both be unique and written to the output)
