
# DO NOT DELCARE main IN THE .globl DIRECTIVE. BREAKS MUNIT!
          .globl strcmp, rec_b_search

          .data

aadvark:  .asciiz "aadvark"
ant:      .asciiz "ant"
elephant: .asciiz "elephant"
gorilla:  .asciiz "gorilla"
hippo:    .asciiz "hippo"
empty:    .asciiz ""

          # make sure the array elements are correctly aligned
          .align 2
sarray:   .word aadvark, ant, elephant, gorilla, hippo
endArray: .word 0  # dummy

.text

main:
            la   $a0, empty
            addi $a1, $a0,   0 # 16
            jal  strcmp

            la   $a0, sarray
            la   $a1, endArray
			addi $a1, $a1,     -4  # point to the last element of sarray
            la   $a2, hippo
            jal  rec_b_search

            addiu      $v0, $zero, 10    # system service 10 is exit
            syscall                      # we are outa here.
 

# a0 - address of string 1
# a1 - address of string 2
strcmp:
######################################################
#  strcmp code here!	
			lb   $t0, 0($a0)			#Take first char(first byte) of address of middle element
			lb   $t1, 0($a1)			#Take first char(first byte) of address of search element to compare with middle element
			bne	 $t0, $t1, difChar		#Compare first chars, if are different then goto label difChar
			beq	 $t0, $zero, equalStrings	#If chars are equal from previous line and one of them is 0(in ascii \0, that means the end of the string) then and the other is 0(in ascii \0, that means the end of the string), after goto label equalStrings
			addi $a0, $a0, 1
			addi $a1, $a1, 1
			j	 strcmp		#make the loop again, now with the next chars(next bytes) of the strings
equalStrings:
			add $v0, $zero, $zero	#if strings are equal then return 0
			jr    $ra			#go back to caller
difChar:    
			slt  $t2, $t0, $t1	#if char of search element is smaller than the other then goto label smaller else return positive number
			bne	 $t2, $zero, smaller
			addi $v0, $zero, 1		#else return positive number
			jr	 $ra	#goto back to caller
smaller:
			addi $v0, $zero, -1  #if char of search element is smaller then return negative number
######################################################
            jr   $ra    #go back to caller


# a0 - base address of array
# a1 - address of last element of array
# a2 - pointer to string to try to match
rec_b_search:
######################################################
#  rec_b_search code here!
			li   $t3, -4
			
			add  $t4, $a0, $a1
			srl  $t4, $t4, 1      #$t4 contains the location of the middle element of the table
			and $t4, $t4, $t3
			
			addi $sp, $sp, -12	#push the values of $a0 (Left Edge) $a1 (Right Edge) $ra (Return Address) from the stack
			sw   $ra, 8($sp)		#to protect them from any changes when calling a subroutine
			sw   $a1, 4($sp)
			sw   $a0, 0($sp)
			
			slt  $t2, $a1, $a0	#Here is the termination condition that is,
			bne  $t2, $zero, notFound	#if the right edge of the table becomes smaller than the left
			
			lw  $a0, 0($t4)	#Here I pass the input values to the subroutine strcmp where $a0 is the address of the middle item and $a1 is the search item
			add  $a1, $zero, $a2
			jal	 strcmp	
						#Call the sabroutine strcmp
			lw   $a0, 0($sp)
			lw   $a1, 4($sp)	#pop the previous values from $ a0 (Left Edge) $ a1 (Right Edge) $ ra (Return Address) stacks
			lw   $ra, 8($sp)
			addi $sp, $sp, 12
			
			beq  $v0, $zero, found		#If the subroutine returns 0 to $v0 then that means the search element was found then goto label found
			
			slt  $t2, $zero, $v0			#if subroutine strcmp returns to $v0 negative number that means the search element located at left sub-table
			bne  $t2, $zero, leftPart			#and then goto label leftPart	
#At this label I select the right sub-table, move $?0 (Left Edge) one position to the right (+4 bytes) of the middle position of the table
rightPart:			 
			add  $a0, $zero, $t4
			addi $a0, $a0, 4
			
			addi $sp, $sp, -4
			sw   $ra, 0($sp)
			
			jal  rec_b_search	#call itself 

			lw   $ra, 0($sp)
			addi $sp, $sp, 4
			jr   $ra  #-----------------------------------
#At this label I select the left sub-table, move $?1 (Right edge) one position to the left (-4 bytes) of the middle position of the table
leftPart:
			add  $a1, $zero, $t4
			addi $a1, $a1, -4
			
			addi $sp, $sp, -4
			sw   $ra, 0($sp)
			
			jal  rec_b_search  #call itself 		

			lw   $ra, 0($sp)
			addi $sp, $sp, 4
			jr   $ra  #-----------------------------------
#We come to this point if the search element is found	
found:
			add  $v0, $zero, $t4	#store the address of the middle element which is the address of search element
			jr    $ra			#go back to caller

notFound:
				
			add $v0, $zero, $zero	#pop the previous values from $ a0 (Left Edge) $ a1 (Right Edge) $ ra (Return Address) stacks	
			lw   $a0, 0($sp) #if search element not included at table then return 0
			lw   $a1, 4($sp)
			lw   $ra, 8($sp)
			addi $sp, $sp, 12
######################################################
            jr   $ra	#go back to caller


