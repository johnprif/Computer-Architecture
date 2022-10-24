
# lab02.asm - Binary search in an array of 32bit signed integers
#   coded in  MIPS assembly using MARS
# for MYΥ-505 - Computer Architecture, Fall 2020
# Department of Computer Science and Engineering, University of Ioannina
# Instructor: Aris Efthymiou

        .globl bsearch # declare the label as global for munit
        
###############################################################################
        .data
sarray: .word 1, 5, 9, 20, 321, 432, 555, 854, 940

###############################################################################
        .text 
# label main freq. breaks munit, so it is removed...
        la         $a0, sarray
        li         $a1, 9
		li         $a2, 1  # the number sought


bsearch:
###############################################################################
# Write you code here.
# Any code above the label bsearch is not executed by the tester! 
###############################################################################
		add		   $t4, $a2, $zero
		addi       $t7, $zero, 1	#True for simplicity
		add 	   $t6, $a1, $zero    #Size of matrix
		sll		   $t6, $t6, 1		
		add	       $t0, $a0, $zero	   	  #Left
		add		   $t1, $a1, $zero	   
		sll        $t1, $t1, 2	   		
		addi	   $t1, $t1, -4  
		add		   $t1, $t1, $t0	#Right
		
startLoop:
		slt		   $t2, $t1, $t0	#if $t1<$t0 then $t2=1 else $t2=0		
		beq		   $t2, $t7, finishLoop	#if $t2==1 then goto finishLoop
		sub 	   $t3, $t1, $t0	#$t3=$t1-$t0
		srl		   $t3, $t3, 1		#$t3=$t3/2 
		add		   $t3, $t3, $t0	#$t3=$t3+$t0 Middle
		srl		   $t6, $t6, 1		#$t6=$t6/2
		and		   $t2, $t6, $t7	#$t2=1 if matrix is odd or $t2=0 if matrix is even
		bne	       $t2, $t7, evenMatrix	#if matrix is even the goto label evenMatrix or if is odd continue to the next line
		lw		   $t5, 0($t3)
		j		   if			#goto label if, if matrix is odd size

evenMatrix:	
		addi	   $t3, $t3, -2		#correction of matrix indexing
		lw 		   $t5, 0($t3)		#load the right value of matrix
		beq		   $t4, $t5, if		#if $a2==$t5 then goto label if else continue to the next line
		slt        $t2, $t4, $t5	#if $a2<$t5 then $t2=1 else $t2=0
		beq		   $t2, $t7, elseIf	#if $t2==$t7 then goto label elseIf else continue to the next line
		addi	   $t3, $t3, +4		#correction of matrix indexing
		lw		   $t5, 0($t3)		#load the right value of matrix


if:
		bne		   $t5, $t4, elseIf			#if $t5!=$a2 then goto elseIf
		add		   $s7, $t3, $zero		#$s7=$t3+0
		j		   exit
elseIf:
		slt 	   $t2, $t4, $t5   #if $a2<$t5 then $t2=1 else $t2=0
		bne		   $t2, $t7, else		#if $t2!=$t7 then goto else
		addi	   $t1, $t3, -4		#New Right
		addi	   $t6, $t6, -1
		j 		   startLoop	#goto label startLoop
else:
		addi	   $t0, $t3, 4	#New Left
		addi	   $t6, $t6, -1
		j		   startLoop	#goto label startLoop
finishLoop:   					
		li		   $s7, 0		

###############################################################################
# End of your code.
###############################################################################
exit:
        addiu      $v0, $zero, 10    # system service 10 is exit
        syscall                      # we are outta here.


