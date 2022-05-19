# Author: Todd Sipe
# Date completed: 3/18/2022
# Machine Learning
# Project 1: Decision Trees
# Partners: Praneeth Marri, Anahita Alibalazadeh (Neither person contributed at all to the writing of this code)

import pandas as pd
import random
import math

# Read in csv files, save as a dataframe
df1 = pd.read_csv('train.csv', header=None, names=['ID', 'SEQUENCE', 'CLASS'])
df2 = pd.read_csv('test.csv', header=None, names=['ID', 'SEQUENCE'])
# Create empty lists for conversion from dataframe to lists
data = []
data2 = []
# Create lists from the dataframes
for item in range(2000):
    nucleotides = list(df1.loc[item, 'SEQUENCE'])
    boundaries = [df1.loc[item, 'CLASS']]
    data.append(nucleotides + boundaries)
for item in range(1190):
    nucleotides = list(df2.loc[item, 'SEQUENCE'])
    data2.append(nucleotides)
# Create list to store index numbers for the output csv file
output_index_list = list(range(2001, 3191))
# Create list to store the classes ascertained by traversing our tree
submission_list = []
# Create list of the 60 attributes for each row of data
attributes = list(range(60))
# Create a list full of 60 -1 values to check if all attributes have been used
attributes_check = [-1] * 60
# Create list of lists that are to be run through the method: "calculate" (starting with the whole data set)
list_of_calculations = [data]
# Create a list of the decision point nodes in the tree from top to bottom, and left to right (looking at a
#   tree from top to bottom, with every node splitting into four edges, A, C, G, and T in that order)
decision_nodes = []
# Create a list of decision nodes and leaf nodes (decisions are attribute number(0-59),
#   and leaves are class (EI, IE, or N)
the_tree = []


# This class is for the tree
# Methods include: add_child, get_level (for printing the tree nicely) and print_tree
class DecisionNode:
    def __init__(self, info):
        self.info = info
        self.children = []
        self.parent = None

    def add_child(self, child):
        child.parent = self
        self.children.append(child)

    def get_level(self):
        level = 0
        p = self.parent
        while p:
            level = level + 1
            p = p.parent
        return level

    def print_tree(self):
        spaces = ' ' * self.get_level() * 2
        print(spaces + self.info)
        if self.children:
            for child in self.children:
                child.print_tree()


# This method changes all D, N, R, and S values found in the train.csv file to G, as G is the
#   common value that these values all share
def deal_with_d_n_r_s_cases(ls):
    for i in range(len(ls)):
        for j in range(60):
            if ls[i][j] == 'D':
                ls[i][j] = 'G'
            if ls[i][j] == 'N':
                ls[i][j] = 'G'
            if ls[i][j] == 'R':
                ls[i][j] = 'G'
            if ls[i][j] == 'S':
                ls[i][j] = 'G'
    return ls


# This statement runs the method to change the D, N, R, and S values for the train.csv file
data = deal_with_d_n_r_s_cases(data)


# This method receives a D, N, R, or S value, and returns a random A, C, G, or T value according to the following:
# D -> A, G, T
# N -> A, C, G, T
# R -> A, G
# S -> C, G
# This method is used to change the D, N, R, and S values in test.csv to a randomly chosen appropriate value
def randomize_d_n_r_s_cases(letter):
    if letter == 'D':
        selections = ['A', 'G', 'T']
        n = random.randint(0, 2)
        return selections[n]
    if letter == 'N':
        selections = ['A', 'C', 'G', 'T']
        n = random.randint(0, 3)
        return selections[n]
    if letter == 'R':
        selections = ['A', 'G']
        n = random.randint(0, 1)
        return selections[n]
    if letter == 'S':
        selections = ['C', 'G']
        n = random.randint(0, 1)
        return selections[n]


# This method takes in a list of DNA sequences and calculates maximum information gain
# This is called recursively through iterating through the list "list_of_calculations" in the while statement found
#   below
# It is currently set to calculate the gini index, rather than the miss classification error, or the entropy
def calculate(ls):
    total = len(ls)
    counter = 0
    gained_info = 0
    gained_info_index = 0
    a_list = []
    c_list = []
    g_list = []
    t_list = []
    a = 0
    c = 0
    g = 0
    t = 0
    a_to_ei = 0
    a_to_ie = 0
    a_to_n = 0
    c_to_ei = 0
    c_to_ie = 0
    c_to_n = 0
    g_to_ei = 0
    g_to_ie = 0
    g_to_n = 0
    t_to_ei = 0
    t_to_ie = 0
    t_to_n = 0
    for att in attributes:
        if att != -1:
            gotten_values = get_values(ls, att)
            info = information_gain(gotten_values[0], gotten_values[1], gotten_values[2], gotten_values[3], total,
                                    gini_attribute(gotten_values[0], gotten_values[1],
                                                   gotten_values[2], gotten_values[3], total),
                                    gini_class(gotten_values[4], gotten_values[5], gotten_values[6], gotten_values[0]),
                                    gini_class(gotten_values[7], gotten_values[8], gotten_values[9], gotten_values[1]),
                                    gini_class(gotten_values[10], gotten_values[11],
                                               gotten_values[12], gotten_values[2]),
                                    gini_class(gotten_values[13], gotten_values[14],
                                               gotten_values[15], gotten_values[3]), )
            if info > gained_info:
                gained_info = info
                gained_info_index = counter
                a = gotten_values[0]
                c = gotten_values[1]
                g = gotten_values[2]
                t = gotten_values[3]
                a_to_ei = gotten_values[4]
                a_to_ie = gotten_values[5]
                a_to_n = gotten_values[6]
                c_to_ei = gotten_values[7]
                c_to_ie = gotten_values[8]
                c_to_n = gotten_values[9]
                g_to_ei = gotten_values[10]
                g_to_ie = gotten_values[11]
                g_to_n = gotten_values[12]
                t_to_ei = gotten_values[13]
                t_to_ie = gotten_values[14]
                t_to_n = gotten_values[15]
                a_list = gotten_values[16]
                c_list = gotten_values[17]
                g_list = gotten_values[18]
                t_list = gotten_values[19]
        counter = counter + 1
    attributes[gained_info_index] = -1
    decision_nodes.append(str(gained_info_index))
    if a == a_to_ei:
        the_tree.append(str(gained_info_index) + ',A,EI')
    elif a == a_to_ie:
        the_tree.append(str(gained_info_index) + ',A,IE')
    elif a == a_to_n:
        the_tree.append(str(gained_info_index) + ',A,N')
    else:
        the_tree.append(str(gained_info_index) + ',A,X')
        list_of_calculations.append(a_list)
    if c == c_to_ei:
        the_tree.append(str(gained_info_index) + ',C,EI')
    elif c == c_to_ie:
        the_tree.append(str(gained_info_index) + ',C,IE')
    elif c == c_to_n:
        the_tree.append(str(gained_info_index) + ',C,N')
    else:
        the_tree.append(str(gained_info_index) + ',C,X')
        list_of_calculations.append(c_list)
    if g == g_to_ei:
        the_tree.append(str(gained_info_index) + ',G,EI')
    elif g == g_to_ie:
        the_tree.append(str(gained_info_index) + ',G,IE')
    elif g == g_to_n:
        the_tree.append(str(gained_info_index) + ',G,N')
    else:
        the_tree.append(str(gained_info_index) + ',G,X')
        list_of_calculations.append(g_list)
    if t == t_to_ei:
        the_tree.append(str(gained_info_index) + ',T,EI')
    elif t == t_to_ie:
        the_tree.append(str(gained_info_index) + ',T,IE')
    elif t == t_to_n:
        the_tree.append(str(gained_info_index) + ',T,N')
    else:
        the_tree.append(str(gained_info_index) + ',T,X')
        list_of_calculations.append(t_list)


# This method takes in a list of DNA sequences and an attribute number
# This returns a list of appropriate numbers to perform calculations on, to get the information gained
def get_values(ls, att):
    ret = []
    a_list = []
    c_list = []
    g_list = []
    t_list = []
    a = 0
    c = 0
    g = 0
    t = 0
    a_to_ei = 0
    a_to_ie = 0
    a_to_n = 0
    c_to_ei = 0
    c_to_ie = 0
    c_to_n = 0
    g_to_ei = 0
    g_to_ie = 0
    g_to_n = 0
    t_to_ei = 0
    t_to_ie = 0
    t_to_n = 0
    for i in range(len(ls)):
        if ls[i][att] == 'A':
            a_list.append(ls[i])
            a = a + 1
            if ls[i][60] == 'EI':
                a_to_ei = a_to_ei + 1
            if ls[i][60] == 'IE':
                a_to_ie = a_to_ie + 1
            if ls[i][60] == 'N':
                a_to_n = a_to_n + 1
        if ls[i][att] == 'C':
            c_list.append(ls[i])
            c = c + 1
            if ls[i][60] == 'EI':
                c_to_ei = c_to_ei + 1
            if ls[i][60] == 'IE':
                c_to_ie = c_to_ie + 1
            if ls[i][60] == 'N':
                c_to_n = c_to_n + 1
        if ls[i][att] == 'G':
            g_list.append(ls[i])
            g = g + 1
            if ls[i][60] == 'EI':
                g_to_ei = g_to_ei + 1
            if ls[i][60] == 'IE':
                g_to_ie = g_to_ie + 1
            if ls[i][60] == 'N':
                g_to_n = g_to_n + 1
        if ls[i][att] == 'T':
            t_list.append(ls[i])
            t = t + 1
            if ls[i][60] == 'EI':
                t_to_ei = t_to_ei + 1
            if ls[i][60] == 'IE':
                t_to_ie = t_to_ie + 1
            if ls[i][60] == 'N':
                t_to_n = t_to_n + 1
    ret.append(a)
    ret.append(c)
    ret.append(g)
    ret.append(t)
    ret.append(a_to_ei)
    ret.append(a_to_ie)
    ret.append(a_to_n)
    ret.append(c_to_ei)
    ret.append(c_to_ie)
    ret.append(c_to_n)
    ret.append(g_to_ei)
    ret.append(g_to_ie)
    ret.append(g_to_n)
    ret.append(t_to_ei)
    ret.append(t_to_ie)
    ret.append(t_to_n)
    ret.append(a_list)
    ret.append(c_list)
    ret.append(g_list)
    ret.append(t_list)
    return ret


# This method calculates the gini index of an attribute
def gini_attribute(a, c, g, t, total):
    if total == 0:
        return 1
    else:
        return 1 - (((a / total) ** 2) + ((c / total) ** 2) + ((g / total) ** 2) + ((t / total) ** 2))


# This method calculates the gini index of a class
def gini_class(ei, ie, n, total):
    if total == 0:
        return 1
    else:
        return 1 - (((ei / total) ** 2) + ((ie / total) ** 2) + ((n / total) ** 2))


# This method calculates the entropy of an attribute
def entropy_attribute(a, c, g, t, total):
    if total == 0:
        return 1
    pa = a / total
    pc = c / total
    pg = g / total
    pt = t / total
    if pa != 0:
        first = -math.log2(pa) * pa
    else:
        first = 0
    if pc != 0:
        second = -math.log2(pc) * pc
    else:
        second = 0
    if pg != 0:
        third = -math.log2(pg) * pg
    else:
        third = 0
    if pc != 0:
        fourth = -math.log2(pt) * pt
    else:
        fourth = 0
    return first + second + third + fourth


# This method calculates the entropy of a class
def entropy_class(ei, ie, n, total):
    if total == 0:
        return 1
    pei = ei / total
    pie = ie / total
    pn = n / total
    if pei != 0:
        first = -math.log2(pei) * pei
    else:
        first = 0
    if pie != 0:
        second = -math.log2(pie) * pie
    else:
        second = 0
    if pn != 0:
        third = -math.log2(pn) * pn
    else:
        third = 0
    return first + second + third


# This method calculates the miss classification for an attribute
def miss_classification_attribute(a, c, g, t, total):
    if total == 0:
        return 1
    pa = a / total
    pc = c / total
    pg = g / total
    pt = t / total
    return 1 - max([pa, pc, pg, pt])


# This method calculates the miss classification for a class
def miss_classification_class(ei, ie, n, total):
    if total == 0:
        return 1
    pei = ei / total
    pie = ie / total
    pn = n / total
    return 1 - max([pei, pie, pn])


# This method calculates the information gain
def information_gain(a, c, g, t, total, imp, ga, gc, gg, gt):
    if total == 0:
        return 0
    else:
        return imp - ((a / total) * ga) - ((c / total) * gc) - ((g / total) * gg) - ((t / total) * gt)


# This while loop is how we recursively call the "calculate" method, traversing the tree that we are creating in a
#   breadth first manner
# At first I tried to recursively call "calculate" with a more traditional method of recursion, but it was creating the
#   tree in a depth first manner
while len(list_of_calculations) > 0 and attributes != attributes_check:
    if len(list_of_calculations) > 0:
        calculate(list_of_calculations[0])
        list_of_calculations.pop(0)
    else:
        break


# This method returns a randomly chosen class, for when we have used all 60 attributes and are in need of filling out
#   the bottom leaves of the tree that were left unresolved
def assign_random_class():
    selections = ['EI', 'IE', 'N']
    n = random.randint(0, 2)
    return selections[n]


# This method builds a tree based on the list "the_tree"
# I am certain there was a way to do this in a clever fashion, but I could not figure that out, so I created the
#   tree manually based on the list of results that I got back in the list "the_tree"
# Every attempt I had at recursively creating the tree with the list I had ended up in a depth first creation, which
# I did not want
def build_tree():
    root = DecisionNode('31')

    # level 5

    zero = DecisionNode('0')
    zero.add_child(DecisionNode('IE'))
    zero.add_child(DecisionNode(assign_random_class()))
    zero.add_child(DecisionNode('EI'))
    zero.add_child(DecisionNode('EI'))

    forty_four = DecisionNode('44')
    forty_four.add_child(DecisionNode('EI'))
    forty_four.add_child(DecisionNode(assign_random_class()))
    forty_four.add_child(DecisionNode('N'))
    forty_four.add_child(DecisionNode('N'))

    # level 4

    sixteen = DecisionNode('16')
    sixteen.add_child(DecisionNode('N'))
    sixteen.add_child(DecisionNode('N'))
    sixteen.add_child(DecisionNode('N'))
    sixteen.add_child(zero)

    fifteen = DecisionNode('15')
    fifteen.add_child(DecisionNode('N'))
    fifteen.add_child(DecisionNode('N'))
    fifteen.add_child(DecisionNode('N'))
    fifteen.add_child(forty_four)

    one = DecisionNode('1')
    one.add_child(DecisionNode(assign_random_class()))
    one.add_child(DecisionNode(assign_random_class()))
    one.add_child(DecisionNode('N'))
    one.add_child(DecisionNode(assign_random_class()))

    two = DecisionNode('2')
    two.add_child(DecisionNode('N'))
    two.add_child(DecisionNode(assign_random_class()))
    two.add_child(DecisionNode('N'))
    two.add_child(DecisionNode(assign_random_class()))

    forty_two = DecisionNode('42')
    forty_two.add_child(DecisionNode('IE'))
    forty_two.add_child(DecisionNode('IE'))
    forty_two.add_child(DecisionNode(assign_random_class()))
    forty_two.add_child(DecisionNode(assign_random_class()))

    forty_three = DecisionNode('43')
    forty_three.add_child(DecisionNode('N'))
    forty_three.add_child(DecisionNode('N'))
    forty_three.add_child(DecisionNode(assign_random_class()))
    forty_three.add_child(DecisionNode('N'))

    fifty_two = DecisionNode('52')
    fifty_two.add_child(DecisionNode('N'))
    fifty_two.add_child(DecisionNode('N'))
    fifty_two.add_child(DecisionNode('N'))
    fifty_two.add_child(DecisionNode(assign_random_class()))

    five = DecisionNode('5')
    five.add_child(DecisionNode(assign_random_class()))
    five.add_child(DecisionNode('IE'))
    five.add_child(DecisionNode('N'))
    five.add_child(DecisionNode(assign_random_class()))

    thirty_eight = DecisionNode('38')
    thirty_eight.add_child(DecisionNode('N'))
    thirty_eight.add_child(DecisionNode(assign_random_class()))
    thirty_eight.add_child(DecisionNode('N'))
    thirty_eight.add_child(DecisionNode('N'))

    fifty_five = DecisionNode('55')
    fifty_five.add_child(DecisionNode(assign_random_class()))
    fifty_five.add_child(DecisionNode(assign_random_class()))
    fifty_five.add_child(DecisionNode(assign_random_class()))
    fifty_five.add_child(DecisionNode('IE'))

    fifty_three = DecisionNode('53')
    fifty_three.add_child(DecisionNode('IE'))
    fifty_three.add_child(DecisionNode(assign_random_class()))
    fifty_three.add_child(DecisionNode(assign_random_class()))
    fifty_three.add_child(DecisionNode(assign_random_class()))

    seven = DecisionNode('7')
    seven.add_child(DecisionNode(assign_random_class()))
    seven.add_child(DecisionNode(assign_random_class()))
    seven.add_child(DecisionNode(assign_random_class()))
    seven.add_child(DecisionNode('IE'))

    thirty_six = DecisionNode('36')
    thirty_six.add_child(DecisionNode(assign_random_class()))
    thirty_six.add_child(DecisionNode(assign_random_class()))
    thirty_six.add_child(DecisionNode(assign_random_class()))
    thirty_six.add_child(DecisionNode(assign_random_class()))

    twenty_two = DecisionNode('22')
    twenty_two.add_child(DecisionNode('N'))
    twenty_two.add_child(DecisionNode(assign_random_class()))
    twenty_two.add_child(DecisionNode(assign_random_class()))
    twenty_two.add_child(DecisionNode(assign_random_class()))

    thirty_five = DecisionNode('35')
    thirty_five.add_child(DecisionNode('N'))
    thirty_five.add_child(DecisionNode('N'))
    thirty_five.add_child(DecisionNode('N'))
    thirty_five.add_child(DecisionNode('IE'))

    thirty_four = DecisionNode('34')
    thirty_four.add_child(DecisionNode('N'))
    thirty_four.add_child(DecisionNode('N'))
    thirty_four.add_child(DecisionNode('N'))
    thirty_four.add_child(DecisionNode(assign_random_class()))

    fourteen = DecisionNode('14')
    fourteen.add_child(DecisionNode('N'))
    fourteen.add_child(DecisionNode(assign_random_class()))
    fourteen.add_child(DecisionNode('N'))
    fourteen.add_child(DecisionNode('N'))

    twenty_six = DecisionNode('26')
    twenty_six.add_child(DecisionNode('IE'))
    twenty_six.add_child(DecisionNode(assign_random_class()))
    twenty_six.add_child(DecisionNode(assign_random_class()))
    twenty_six.add_child(DecisionNode(assign_random_class()))

    fifty_seven = DecisionNode('57')
    fifty_seven.add_child(DecisionNode('IE'))
    fifty_seven.add_child(DecisionNode(assign_random_class()))
    fifty_seven.add_child(DecisionNode(assign_random_class()))
    fifty_seven.add_child(DecisionNode('N'))

    thirty_seven = DecisionNode('37')
    thirty_seven.add_child(DecisionNode('N'))
    thirty_seven.add_child(DecisionNode(assign_random_class()))
    thirty_seven.add_child(DecisionNode('N'))
    thirty_seven.add_child(DecisionNode('IE'))

    ten = DecisionNode('10')
    ten.add_child(DecisionNode(assign_random_class()))
    ten.add_child(DecisionNode('N'))
    ten.add_child(DecisionNode('N'))
    ten.add_child(DecisionNode('N'))

    twelve = DecisionNode('12')
    twelve.add_child(DecisionNode('N'))
    twelve.add_child(DecisionNode(assign_random_class()))
    twelve.add_child(DecisionNode('N'))
    twelve.add_child(DecisionNode('N'))

    thirty_nine = DecisionNode('39')
    thirty_nine.add_child(DecisionNode(assign_random_class()))
    thirty_nine.add_child(DecisionNode('N'))
    thirty_nine.add_child(DecisionNode(assign_random_class()))
    thirty_nine.add_child(DecisionNode('N'))

    eleven = DecisionNode('11')
    eleven.add_child(DecisionNode(assign_random_class()))
    eleven.add_child(DecisionNode(assign_random_class()))
    eleven.add_child(DecisionNode(assign_random_class()))
    eleven.add_child(DecisionNode(assign_random_class()))

    four = DecisionNode('4')
    four.add_child(DecisionNode(assign_random_class()))
    four.add_child(DecisionNode('N'))
    four.add_child(DecisionNode('N'))
    four.add_child(DecisionNode('N'))

    forty_eight = DecisionNode('48')
    forty_eight.add_child(DecisionNode(assign_random_class()))
    forty_eight.add_child(DecisionNode(assign_random_class()))
    forty_eight.add_child(DecisionNode(assign_random_class()))
    forty_eight.add_child(DecisionNode(assign_random_class()))

    thirty_three = DecisionNode('33')
    thirty_three.add_child(DecisionNode('EI'))
    thirty_three.add_child(DecisionNode(assign_random_class()))
    thirty_three.add_child(DecisionNode(assign_random_class()))
    thirty_three.add_child(DecisionNode(assign_random_class()))

    forty_nine = DecisionNode('49')
    forty_nine.add_child(DecisionNode(assign_random_class()))
    forty_nine.add_child(DecisionNode('EI'))
    forty_nine.add_child(DecisionNode('EI'))
    forty_nine.add_child(DecisionNode(assign_random_class()))

    fifty_eight = DecisionNode('58')
    fifty_eight.add_child(DecisionNode('EI'))
    fifty_eight.add_child(DecisionNode(assign_random_class()))
    fifty_eight.add_child(DecisionNode(assign_random_class()))
    fifty_eight.add_child(DecisionNode(assign_random_class()))

    fifty = DecisionNode('50')
    fifty.add_child(DecisionNode(assign_random_class()))
    fifty.add_child(DecisionNode(assign_random_class()))
    fifty.add_child(DecisionNode(assign_random_class()))
    fifty.add_child(DecisionNode(assign_random_class()))

    thirty = DecisionNode('30')
    thirty.add_child(DecisionNode(assign_random_class()))
    thirty.add_child(DecisionNode(assign_random_class()))
    thirty.add_child(DecisionNode(assign_random_class()))
    thirty.add_child(DecisionNode('N'))

    forty_five = DecisionNode('45')
    forty_five.add_child(DecisionNode(assign_random_class()))
    forty_five.add_child(DecisionNode(assign_random_class()))
    forty_five.add_child(DecisionNode(assign_random_class()))
    forty_five.add_child(DecisionNode(assign_random_class()))

    twenty_four = DecisionNode('24')
    twenty_four.add_child(DecisionNode('N'))
    twenty_four.add_child(DecisionNode(assign_random_class()))
    twenty_four.add_child(DecisionNode('N'))
    twenty_four.add_child(DecisionNode(assign_random_class()))

    forty = DecisionNode('40')
    forty.add_child(DecisionNode(assign_random_class()))
    forty.add_child(DecisionNode('N'))
    forty.add_child(DecisionNode(assign_random_class()))
    forty.add_child(DecisionNode(assign_random_class()))

    twenty = DecisionNode('20')
    twenty.add_child(DecisionNode(assign_random_class()))
    twenty.add_child(DecisionNode(assign_random_class()))
    twenty.add_child(DecisionNode(assign_random_class()))
    twenty.add_child(DecisionNode(assign_random_class()))

    twenty_one = DecisionNode('21')
    twenty_one.add_child(DecisionNode(assign_random_class()))
    twenty_one.add_child(DecisionNode(assign_random_class()))
    twenty_one.add_child(DecisionNode(assign_random_class()))
    twenty_one.add_child(DecisionNode(assign_random_class()))

    eight = DecisionNode('8')
    eight.add_child(DecisionNode(assign_random_class()))
    eight.add_child(DecisionNode(assign_random_class()))
    eight.add_child(DecisionNode(assign_random_class()))
    eight.add_child(DecisionNode(assign_random_class()))

    fifty_four = DecisionNode('54')
    fifty_four.add_child(DecisionNode(assign_random_class()))
    fifty_four.add_child(DecisionNode(assign_random_class()))
    fifty_four.add_child(DecisionNode(assign_random_class()))
    fifty_four.add_child(DecisionNode(assign_random_class()))

    fifty_nine = DecisionNode('59')
    fifty_nine.add_child(DecisionNode(assign_random_class()))
    fifty_nine.add_child(DecisionNode(assign_random_class()))
    fifty_nine.add_child(DecisionNode(assign_random_class()))
    fifty_nine.add_child(DecisionNode(assign_random_class()))

    # level three

    three = DecisionNode('3')
    three.add_child(DecisionNode('N'))
    three.add_child(DecisionNode('N'))
    three.add_child(sixteen)
    three.add_child(DecisionNode('N'))

    twenty_five = DecisionNode('25')
    twenty_five.add_child(fifteen)
    twenty_five.add_child(one)
    twenty_five.add_child(five)
    twenty_five.add_child(forty_two)

    forty_seven = DecisionNode('47')
    forty_seven.add_child(DecisionNode('N'))
    forty_seven.add_child(forty_three)
    forty_seven.add_child(DecisionNode('N'))
    forty_seven.add_child(DecisionNode('N'))

    seventeen = DecisionNode('17')
    seventeen.add_child(fifty_two)
    seventeen.add_child(five)
    seventeen.add_child(thirty_eight)
    seventeen.add_child(fifty_five)

    fifty_six = DecisionNode('56')
    fifty_six.add_child(fifty_three)
    fifty_six.add_child(seven)
    fifty_six.add_child(thirty_six)
    fifty_six.add_child(twenty_two)

    forty_six = DecisionNode('46')
    forty_six.add_child(thirty_five)
    forty_six.add_child(DecisionNode('N'))
    forty_six.add_child(DecisionNode('N'))
    forty_six.add_child(DecisionNode('N'))

    six = DecisionNode('6')
    six.add_child(DecisionNode('N'))
    six.add_child(DecisionNode('N'))
    six.add_child(DecisionNode('N'))
    six.add_child(thirty_four)

    eighteen = DecisionNode('18')
    eighteen.add_child(fourteen)
    eighteen.add_child(twenty_six)
    eighteen.add_child(DecisionNode('N'))
    eighteen.add_child(fifty_seven)

    fifty_one = DecisionNode('51')
    fifty_one.add_child(DecisionNode('N'))
    fifty_one.add_child(thirty_seven)
    fifty_one.add_child(ten)
    fifty_one.add_child(twelve)

    twenty_three = DecisionNode('23')
    twenty_three.add_child(thirty_nine)
    twenty_three.add_child(eleven)
    twenty_three.add_child(four)
    twenty_three.add_child(forty_eight)

    forty_one = DecisionNode('41')
    forty_one.add_child(thirty_three)
    forty_one.add_child(forty_nine)
    forty_one.add_child(fifty_eight)
    forty_one.add_child(fifty)

    thirteen = DecisionNode('13')
    thirteen.add_child(thirty)
    thirteen.add_child(forty_five)
    thirteen.add_child(twenty_four)
    thirteen.add_child(forty)

    nine = DecisionNode('9')
    nine.add_child(twenty)
    nine.add_child(twenty_one)
    nine.add_child(eight)
    nine.add_child(fifty_four)

    twenty_nine = DecisionNode('29')
    twenty_nine.add_child(DecisionNode('N'))
    twenty_nine.add_child(DecisionNode('N'))
    twenty_nine.add_child(fifty_nine)
    twenty_nine.add_child(DecisionNode('N'))

    # level 2

    twenty_seven = DecisionNode('27')
    twenty_seven.add_child(three)
    twenty_seven.add_child(twenty_five)
    twenty_seven.add_child(forty_seven)
    twenty_seven.add_child(seventeen)

    twenty_eight = DecisionNode('28')
    twenty_eight.add_child(fifty_six)
    twenty_eight.add_child(DecisionNode('N'))
    twenty_eight.add_child(forty_six)
    twenty_eight.add_child(DecisionNode('N'))

    nineteen = DecisionNode('19')
    nineteen.add_child(six)
    nineteen.add_child(eighteen)
    nineteen.add_child(fifty_one)
    nineteen.add_child(twenty_three)

    thirty_two = DecisionNode('32')
    thirty_two.add_child(forty_one)
    thirty_two.add_child(thirteen)
    thirty_two.add_child(nine)
    thirty_two.add_child(twenty_nine)

    # level 1

    root.add_child(twenty_seven)
    root.add_child(twenty_eight)
    root.add_child(nineteen)
    root.add_child(thirty_two)

    return root


# These are the calls to create and print the tree
tree = build_tree()
tree.print_tree()


# This method changes all the D, N, R, and S values to a random appropriately chosen value of A, C, G, or T
def deal_with_d_n_r_s_cases_output(ls):
    for i in range(len(ls)):
        for j in range(60):
            if ls[i][j] == 'D':
                ls[i][j] = randomize_d_n_r_s_cases('D')
            if ls[i][j] == 'N':
                ls[i][j] = randomize_d_n_r_s_cases('N')
            if ls[i][j] == 'R':
                ls[i][j] = randomize_d_n_r_s_cases('R')
            if ls[i][j] == 'S':
                ls[i][j] = randomize_d_n_r_s_cases('S')
    return ls


# This calls the method to change all the D, N, R, and S values to a random appropriately chosen value of A, C, G, or T
data2 = deal_with_d_n_r_s_cases_output(data2)


# This is a series of if statements that reflects the tree created in the method "build_tree"
# Once again, it eluded me how to create this in a clever way, to be able to recreate this process with a
#   different approach
# This was carefully handwritten
def the_if_tree():
    for i in range(1190):
        if data2[i][31] == 'A':
            if data2[i][27] == 'A':
                if data2[i][3] == 'A':
                    submission_list.append('N')
                if data2[i][3] == 'C':
                    submission_list.append('N')
                if data2[i][3] == 'G':
                    if data2[i][16] == 'A':
                        submission_list.append('N')
                    if data2[i][16] == 'C':
                        submission_list.append('N')
                    if data2[i][16] == 'G':
                        submission_list.append('N')
                    if data2[i][16] == 'T':
                        if data2[i][0] == 'A':
                            submission_list.append('IE')
                        if data2[i][0] == 'C':
                            submission_list.append(assign_random_class())
                        if data2[i][0] == 'G':
                            submission_list.append('EI')
                        if data2[i][0] == 'T':
                            submission_list.append('EI')
                if data2[i][3] == 'T':
                    submission_list.append('N')
            if data2[i][27] == 'C':
                if data2[i][25] == 'A':
                    if data2[i][15] == 'A':
                        submission_list.append('N')
                    if data2[i][15] == 'C':
                        submission_list.append('N')
                    if data2[i][15] == 'G':
                        submission_list.append('N')
                    if data2[i][15] == 'T':
                        if data2[i][44] == 'A':
                            submission_list.append('EI')
                        if data2[i][44] == 'C':
                            submission_list.append(assign_random_class())
                        if data2[i][44] == 'G':
                            submission_list.append('N')
                        if data2[i][44] == 'T':
                            submission_list.append('N')
                if data2[i][25] == 'C':
                    if data2[i][1] == 'A':
                        submission_list.append(assign_random_class())
                    if data2[i][1] == 'C':
                        submission_list.append(assign_random_class())
                    if data2[i][1] == 'G':
                        submission_list.append('N')
                    if data2[i][1] == 'T':
                        submission_list.append(assign_random_class())
                if data2[i][25] == 'G':
                    if data2[i][2] == 'A':
                        submission_list.append('N')
                    if data2[i][2] == 'C':
                        submission_list.append(assign_random_class())
                    if data2[i][2] == 'G':
                        submission_list.append('N')
                    if data2[i][2] == 'T':
                        submission_list.append(assign_random_class())
                if data2[i][25] == 'T':
                    if data2[i][42] == 'A':
                        submission_list.append('IE')
                    if data2[i][42] == 'C':
                        submission_list.append('IE')
                    if data2[i][42] == 'G':
                        submission_list.append(assign_random_class())
                    if data2[i][42] == 'T':
                        submission_list.append(assign_random_class())
            if data2[i][27] == 'G':
                if data2[i][47] == 'A':
                    submission_list.append('N')
                if data2[i][47] == 'C':
                    if data2[i][43] == 'A':
                        submission_list.append('N')
                    if data2[i][43] == 'C':
                        submission_list.append('N')
                    if data2[i][43] == 'G':
                        submission_list.append(assign_random_class())
                    if data2[i][43] == 'T':
                        submission_list.append('N')
                if data2[i][47] == 'G':
                    submission_list.append('N')
                if data2[i][47] == 'T':
                    submission_list.append('N')
            if data2[i][27] == 'T':
                if data2[i][17] == 'A':
                    if data2[i][52] == 'A':
                        submission_list.append('N')
                    if data2[i][52] == 'C':
                        submission_list.append('N')
                    if data2[i][52] == 'G':
                        submission_list.append('N')
                    if data2[i][52] == 'T':
                        submission_list.append(assign_random_class())
                if data2[i][17] == 'C':
                    if data2[i][5] == 'A':
                        submission_list.append(assign_random_class())
                    if data2[i][5] == 'C':
                        submission_list.append('IE')
                    if data2[i][5] == 'G':
                        submission_list.append('N')
                    if data2[i][5] == 'T':
                        submission_list.append(assign_random_class())
                if data2[i][17] == 'G':
                    if data2[i][38] == 'A':
                        submission_list.append('N')
                    if data2[i][38] == 'C':
                        submission_list.append(assign_random_class())
                    if data2[i][38] == 'G':
                        submission_list.append('N')
                    if data2[i][38] == 'T':
                        submission_list.append('N')
                if data2[i][17] == 'T':
                    if data2[i][55] == 'A':
                        submission_list.append(assign_random_class())
                    if data2[i][55] == 'C':
                        submission_list.append(assign_random_class())
                    if data2[i][55] == 'G':
                        submission_list.append(assign_random_class())
                    if data2[i][55] == 'T':
                        submission_list.append('IE')
        if data2[i][31] == 'C':
            if data2[i][28] == 'A':
                if data2[i][56] == 'A':
                    if data2[i][53] == 'A':
                        submission_list.append('IE')
                    if data2[i][53] == 'C':
                        submission_list.append(assign_random_class())
                    if data2[i][53] == 'G':
                        submission_list.append(assign_random_class())
                    if data2[i][53] == 'T':
                        submission_list.append(assign_random_class())
                if data2[i][56] == 'C':
                    if data2[i][7] == 'A':
                        submission_list.append(assign_random_class())
                    if data2[i][7] == 'C':
                        submission_list.append(assign_random_class())
                    if data2[i][7] == 'G':
                        submission_list.append(assign_random_class())
                    if data2[i][7] == 'T':
                        submission_list.append('IE')
                if data2[i][56] == 'G':
                    if data2[i][36] == 'A':
                        submission_list.append(assign_random_class())
                    if data2[i][36] == 'C':
                        submission_list.append(assign_random_class())
                    if data2[i][36] == 'G':
                        submission_list.append(assign_random_class())
                    if data2[i][36] == 'T':
                        submission_list.append(assign_random_class())
                if data2[i][56] == 'T':
                    if data2[i][22] == 'A':
                        submission_list.append('N')
                    if data2[i][22] == 'C':
                        submission_list.append(assign_random_class())
                    if data2[i][22] == 'G':
                        submission_list.append(assign_random_class())
                    if data2[i][22] == 'T':
                        submission_list.append(assign_random_class())
            if data2[i][28] == 'C':
                submission_list.append('N')
            if data2[i][28] == 'G':
                if data2[i][46] == 'A':
                    if data2[i][35] == 'A':
                        submission_list.append('N')
                    if data2[i][35] == 'C':
                        submission_list.append('N')
                    if data2[i][35] == 'G':
                        submission_list.append('N')
                    if data2[i][35] == 'T':
                        submission_list.append('IE')
                if data2[i][46] == 'C':
                    submission_list.append('N')
                if data2[i][46] == 'G':
                    submission_list.append('N')
                if data2[i][46] == 'T':
                    submission_list.append('N')
            if data2[i][28] == 'T':
                submission_list.append('N')
        if data2[i][31] == 'G':
            if data2[i][19] == 'A':
                if data2[i][6] == 'A':
                    submission_list.append('N')
                if data2[i][6] == 'C':
                    submission_list.append('N')
                if data2[i][6] == 'G':
                    submission_list.append('N')
                if data2[i][6] == 'T':
                    if data2[i][34] == 'A':
                        submission_list.append('N')
                    if data2[i][34] == 'C':
                        submission_list.append('N')
                    if data2[i][34] == 'G':
                        submission_list.append('N')
                    if data2[i][34] == 'T':
                        submission_list.append(assign_random_class())
            if data2[i][19] == 'C':
                if data2[i][18] == 'A':
                    if data2[i][14] == 'A':
                        submission_list.append('N')
                    if data2[i][14] == 'C':
                        submission_list.append(assign_random_class())
                    if data2[i][14] == 'G':
                        submission_list.append('N')
                    if data2[i][14] == 'T':
                        submission_list.append('N')
                if data2[i][18] == 'C':
                    if data2[i][26] == 'A':
                        submission_list.append('IE')
                    if data2[i][26] == 'C':
                        submission_list.append(assign_random_class())
                    if data2[i][26] == 'G':
                        submission_list.append(assign_random_class())
                    if data2[i][26] == 'T':
                        submission_list.append(assign_random_class())
                if data2[i][18] == 'G':
                    submission_list.append('N')
                if data2[i][18] == 'T':
                    if data2[i][57] == 'A':
                        submission_list.append('IE')
                    if data2[i][57] == 'C':
                        submission_list.append(assign_random_class())
                    if data2[i][57] == 'G':
                        submission_list.append(assign_random_class())
                    if data2[i][57] == 'T':
                        submission_list.append('N')
            if data2[i][19] == 'G':
                if data2[i][51] == 'A':
                    submission_list.append('N')
                if data2[i][51] == 'C':
                    if data2[i][37] == 'A':
                        submission_list.append('N')
                    if data2[i][37] == 'C':
                        submission_list.append(assign_random_class())
                    if data2[i][37] == 'G':
                        submission_list.append('N')
                    if data2[i][37] == 'T':
                        submission_list.append('IE')
                if data2[i][51] == 'G':
                    if data2[i][10] == 'A':
                        submission_list.append(assign_random_class())
                    if data2[i][10] == 'C':
                        submission_list.append('N')
                    if data2[i][10] == 'G':
                        submission_list.append('N')
                    if data2[i][10] == 'T':
                        submission_list.append('N')
                if data2[i][51] == 'T':
                    if data2[i][12] == 'A':
                        submission_list.append('N')
                    if data2[i][12] == 'C':
                        submission_list.append(assign_random_class())
                    if data2[i][12] == 'G':
                        submission_list.append('N')
                    if data2[i][12] == 'T':
                        submission_list.append('N')
            if data2[i][19] == 'T':
                if data2[i][23] == 'A':
                    if data2[i][39] == 'A':
                        submission_list.append(assign_random_class())
                    if data2[i][39] == 'C':
                        submission_list.append('N')
                    if data2[i][39] == 'G':
                        submission_list.append(assign_random_class())
                    if data2[i][39] == 'T':
                        submission_list.append('N')
                if data2[i][23] == 'C':
                    if data2[i][11] == 'A':
                        submission_list.append(assign_random_class())
                    if data2[i][11] == 'C':
                        submission_list.append(assign_random_class())
                    if data2[i][11] == 'G':
                        submission_list.append(assign_random_class())
                    if data2[i][11] == 'T':
                        submission_list.append(assign_random_class())
                if data2[i][23] == 'G':
                    if data2[i][4] == 'A':
                        submission_list.append(assign_random_class())
                    if data2[i][4] == 'C':
                        submission_list.append('N')
                    if data2[i][4] == 'G':
                        submission_list.append('N')
                    if data2[i][4] == 'T':
                        submission_list.append('N')
                if data2[i][23] == 'T':
                    if data2[i][48] == 'A':
                        submission_list.append(assign_random_class())
                    if data2[i][48] == 'C':
                        submission_list.append(assign_random_class())
                    if data2[i][48] == 'G':
                        submission_list.append(assign_random_class())
                    if data2[i][48] == 'T':
                        submission_list.append(assign_random_class())
        if data2[i][31] == 'T':
            if data2[i][32] == 'A':
                if data2[i][41] == 'A':
                    if data2[i][33] == 'A':
                        submission_list.append('EI')
                    if data2[i][33] == 'C':
                        submission_list.append(assign_random_class())
                    if data2[i][33] == 'G':
                        submission_list.append(assign_random_class())
                    if data2[i][33] == 'T':
                        submission_list.append(assign_random_class())
                if data2[i][41] == 'C':
                    if data2[i][49] == 'A':
                        submission_list.append(assign_random_class())
                    if data2[i][49] == 'C':
                        submission_list.append('EI')
                    if data2[i][49] == 'G':
                        submission_list.append('EI')
                    if data2[i][49] == 'T':
                        submission_list.append(assign_random_class())
                if data2[i][41] == 'G':
                    if data2[i][58] == 'A':
                        submission_list.append('EI')
                    if data2[i][58] == 'C':
                        submission_list.append(assign_random_class())
                    if data2[i][58] == 'G':
                        submission_list.append(assign_random_class())
                    if data2[i][58] == 'T':
                        submission_list.append(assign_random_class())
                if data2[i][41] == 'T':
                    if data2[i][50] == 'A':
                        submission_list.append(assign_random_class())
                    if data2[i][50] == 'C':
                        submission_list.append(assign_random_class())
                    if data2[i][50] == 'G':
                        submission_list.append(assign_random_class())
                    if data2[i][50] == 'T':
                        submission_list.append(assign_random_class())
            if data2[i][32] == 'C':
                if data2[i][13] == 'A':
                    if data2[i][30] == 'A':
                        submission_list.append(assign_random_class())
                    if data2[i][30] == 'C':
                        submission_list.append(assign_random_class())
                    if data2[i][30] == 'G':
                        submission_list.append(assign_random_class())
                    if data2[i][30] == 'T':
                        submission_list.append('N')
                if data2[i][13] == 'C':
                    if data2[i][45] == 'A':
                        submission_list.append(assign_random_class())
                    if data2[i][45] == 'C':
                        submission_list.append(assign_random_class())
                    if data2[i][45] == 'G':
                        submission_list.append(assign_random_class())
                    if data2[i][45] == 'T':
                        submission_list.append(assign_random_class())
                if data2[i][13] == 'G':
                    if data2[i][24] == 'A':
                        submission_list.append('N')
                    if data2[i][24] == 'C':
                        submission_list.append(assign_random_class())
                    if data2[i][24] == 'G':
                        submission_list.append('N')
                    if data2[i][24] == 'T':
                        submission_list.append(assign_random_class())
                if data2[i][13] == 'T':
                    if data2[i][40] == 'A':
                        submission_list.append(assign_random_class())
                    if data2[i][40] == 'C':
                        submission_list.append('N')
                    if data2[i][40] == 'G':
                        submission_list.append(assign_random_class())
                    if data2[i][40] == 'T':
                        submission_list.append(assign_random_class())
            if data2[i][32] == 'G':
                if data2[i][9] == 'A':
                    if data2[i][20] == 'A':
                        submission_list.append(assign_random_class())
                    if data2[i][20] == 'C':
                        submission_list.append(assign_random_class())
                    if data2[i][20] == 'G':
                        submission_list.append(assign_random_class())
                    if data2[i][20] == 'T':
                        submission_list.append(assign_random_class())
                if data2[i][9] == 'C':
                    if data2[i][21] == 'A':
                        submission_list.append(assign_random_class())
                    if data2[i][21] == 'C':
                        submission_list.append(assign_random_class())
                    if data2[i][21] == 'G':
                        submission_list.append(assign_random_class())
                    if data2[i][21] == 'T':
                        submission_list.append(assign_random_class())
                if data2[i][9] == 'G':
                    if data2[i][8] == 'A':
                        submission_list.append(assign_random_class())
                    if data2[i][8] == 'C':
                        submission_list.append(assign_random_class())
                    if data2[i][8] == 'G':
                        submission_list.append(assign_random_class())
                    if data2[i][8] == 'T':
                        submission_list.append(assign_random_class())
                if data2[i][9] == 'T':
                    if data2[i][54] == 'A':
                        submission_list.append(assign_random_class())
                    if data2[i][54] == 'C':
                        submission_list.append(assign_random_class())
                    if data2[i][54] == 'G':
                        submission_list.append(assign_random_class())
                    if data2[i][54] == 'T':
                        submission_list.append(assign_random_class())
            if data2[i][32] == 'T':
                if data2[i][29] == 'A':
                    submission_list.append('N')
                if data2[i][29] == 'C':
                    submission_list.append('N')
                if data2[i][29] == 'G':
                    if data2[i][59] == 'A':
                        submission_list.append(assign_random_class())
                    if data2[i][59] == 'C':
                        submission_list.append(assign_random_class())
                    if data2[i][59] == 'G':
                        submission_list.append(assign_random_class())
                    if data2[i][59] == 'T':
                        submission_list.append(assign_random_class())
                if data2[i][29] == 'T':
                    submission_list.append('N')


# This statement calls the method "the_if_tree"
the_if_tree()


# These statements create the output file in the proper format for submission by zipping the ID list with the list
# of predicted class values for each ID
df3 = pd.DataFrame(list(zip(output_index_list, submission_list)))
df3.to_csv('output.csv', header=False, index=False)
