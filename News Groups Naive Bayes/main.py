import random
import numpy as np
import time
import math
import pandas as pd
import scipy.sparse
import scipy.sparse.linalg
import re
from scipy import sparse
from scipy.sparse import csr_matrix


# LAST STEP: NAIVE BAYES PORTION
# timer to time each run of the program
start_time = time.time()
# reading in the csv files that are relevant to running the NB algorithm
df_words = pd.read_csv('words_count_by_class')
df_mle = pd.read_csv('MLE_P(Yk)_Probabilities')
df_testing_data = pd.read_csv('data_all_testing.csv')
df_unique = pd.read_csv('unique_count_by_class')
# the list of unique words by class needed clean-up
list_of_unique_word_count_by_class = df_unique.values.tolist()
list_of_unique_word_count_by_class = list_of_unique_word_count_by_class[0]
# declaring the values for use in the MAP calculation
v = len(df_words) - 1
beta = 1


# this method calculates the log of the P(Y|X)
def calculate_py(p):
    return math.log(p)


# this method calculates the log of the MAP
def calculate(c, u):
    return math.log((c + beta)/(u + 1))


# this method returns the predicted class that the document should belong to
def classify(ls):
    x = ls.index(max(ls)) + 1
    return x


# this timer counter was used to gauge how fast our program was running while it was being run
timer_counter = 0
# this list is going to be lists of two parameters, the ID and the predicted class
output = []
# this groupby breaks up the testing data into small batches based on the document ID
df1 = df_testing_data.groupby('ID')
# test data document IDs went from 12001 to 18774
for i in range(12001, 18775):
    timer_counter = timer_counter + 1
    print(timer_counter)
    # set the current groupby object by document ID
    current = df1.get_group(i)
    # save length of the group
    word_count = len(current)
    # the list of possible classes that could be selected as a prediction, cumulative MAP values
    list_of_possible_classes = []
    # iterate through each class
    for j in range(20):
        # list of words, to calculate the MAP, starting with the MLE
        list_of_xi = [calculate_py(df_mle.iat[j, 0])]
        # this loop calculates the appropriate MAP values for each xi, or word, in the class currently being looked at
        for k in range(word_count):
            # this is the word from the test set
            word_from_test = current.iat[k, 1]
            # this is the count of the word from the test set (which actually doesn't even need to exist in this algo)
            count_from_test = current.iat[k, 2]
            # this gives us the count of the word from the test set, from the training set
            count_from_train = df_words.iloc[word_from_test, j + 1]
            # calculate current MAP value, add to list
            pxi = calculate(int(count_from_train), list_of_unique_word_count_by_class[j])
            list_of_xi.append(pxi)
        # add up the list
        pxi_in_current = sum(list_of_xi)
        # add the calculated MAP values for the class into the list of possible classes
        list_of_possible_classes.append(pxi_in_current)
    # add the index and class prediction to the output list
    temp = [i, classify(list_of_possible_classes)]
    output.append(temp)
# create the csv file for submission
df_out = pd.DataFrame(output, columns=['id', 'class'])
df_out.to_csv('submission', encoding='utf-8', index=False)
end_time = time.time()
print("time to execute: " + str(end_time - start_time) + "seconds.")


# # CREATING THE CONFUSION MATRIX
# df_test = pd.read_csv('conf_matrix_output_1', header=None)
# df_actual = pd.read_csv('conf_matrix_testing_set')
# df_actual = df_actual.drop(['Word', 'Count'], axis=1)
# df_test_list = pd.read_csv('conf_matrix_test_list.csv', names=['numbers'])
# test_list_actual_values = df_test_list['numbers'].tolist()
# ls = []
# ls0 = []
# ls1 = []
# ls2 = df_actual['ID'].tolist()
# ls3 = df_actual['Class'].tolist()
# for i in range(len(ls2)):
#     if ls2[i] not in ls0:
#         ls0.append(ls2[i])
#         ls1.append(ls3[i])
# ls.append(ls0)
# ls.append(ls1)
# df_test_actual = pd.DataFrame(ls)
# df_test_actual = df_test_actual.transpose()
# the_matrix = np.zeros((20, 20))
# for i in range(len(df_test)):
#     the_matrix[int(df_test[1][i]) - 1][int(df_test_actual[1][i]) - 1] += 1
# df_final = pd.DataFrame(the_matrix)
# df_final.columns = ['1', '2', '3', '4', '5', '6',
#                     '7', '8', '9', '10', '11', '12',
#                     '13', '14', '15', '16', '17',
#                     '18', '19', '20']
# df_final.index = ['1', '2', '3', '4', '5', '6',
#                   '7', '8', '9', '10', '11', '12',
#                   '13', '14', '15', '16', '17',
#                   '18', '19', '20']
# df_final = df_final.astype(int)
# print(df_final)
# df_final.to_csv('confusion_matrix', sep='\t', mode='a')


# # QUESTION 4 LAST STEP: NAIVE BAYES PORTION
# # timer to time each run of the program
# start_time = time.time()
# # reading in the csv files that are relevant to running the NB algorithm
# df_words = pd.read_csv('conf_matrix_words_count_by_class')
# df_mle = pd.read_csv('conf_matrix_MLE_P(Yk)_Probabilities')
# df_testing_data = pd.read_csv('conf_matrix_testing_set_no_class')
# df_unique = pd.read_csv('conf_matrix_unique_word_counts_by_class')
# df_test_list = pd.read_csv('conf_matrix_test_list.csv', names=['numbers'])
# test_list_actual_values = df_test_list['numbers'].tolist()
# # the list of unique words by class needed clean-up
# list_of_unique_word_count_by_class = df_unique.values.tolist()
# list_of_unique_word_count_by_class = list_of_unique_word_count_by_class[0]
# # declaring the values for use in the MAP calculation
# v = len(df_words) - 1
# beta = 1/v
#
#
# # this method calculates the log of the P(Y|X)
# def calculate_py(p):
#     return math.log(p)
#
#
# # this method calculates the log of the MAP
# def calculate(c, u):
#     return math.log((c + beta)/(u + 1))
#
#
# # this method returns the predicted class that the document should belong to
# def classify(ls):
#     x = ls.index(max(ls)) + 1
#     return x
#
#
# # this timer counter was used to gauge how fast our program was running while it was being run
# timer_counter = 0
# # this list is going to be lists of two parameters, the ID and the predicted class
# output = []
# # this groupby breaks up the testing data into small batches based on the document ID
# df1 = df_testing_data.groupby('ID')
# #
# for i in test_list_actual_values:
#     timer_counter = timer_counter + 1
#     print(timer_counter)
#     # set the current groupby object by document ID
#     current = df1.get_group(i)
#     # save length of the group
#     word_count = len(current)
#     # the list of possible classes that could be selected as a prediction, cumulative MAP values
#     list_of_possible_classes = []
#     # iterate through each class
#     for j in range(20):
#         # list of words, to calculate the MAP, starting with the MLE
#         list_of_xi = [calculate_py(df_mle.iat[j, 0])]
#         # this loop calculates the appropriate MAP values for each xi, or word, in the class currently being looked at
#         for k in range(word_count):
#             # this is the word from the test set
#             word_from_test = current.iat[k, 1]
#             # this is the count of the word from the test set (which actually doesn't even need to exist in this algo)
#             count_from_test = current.iat[k, 2]
#             # this gives us the count of the word from the test set, from the training set
#             count_from_train = df_words.iloc[word_from_test, j + 1]
#             # calculate current MAP value, add to list
#             pxi = calculate(int(count_from_train), list_of_unique_word_count_by_class[j])
#             list_of_xi.append(pxi)
#         # add up the list
#         pxi_in_current = sum(list_of_xi)
#         # add the calculated MAP values for the class into the list of possible classes
#         list_of_possible_classes.append(pxi_in_current)
#     # add the index and class prediction to the output list
#     temp = [i, classify(list_of_possible_classes)]
#     output.append(temp)
# # create the csv file for submission
# df_out = pd.DataFrame(output, columns=['id', 'class'])
# df_out.to_csv('conf_matrix_output_1', encoding='utf-8', index=False)
# end_time = time.time()
# print("time to execute: " + str(end_time - start_time) + "seconds.")


# QUESTION 4 STEP 8: REMOVE THE CLASSIFIER FROM conf_matrix_testing_set
# df = pd.read_csv("conf_matrix_testing_set")
# df.drop(['Class'], axis=1, inplace=True)
# print(df)
# df.to_csv('conf_matrix_testing_set_no_class', encoding='utf-8', index=False)


# QUESTION 4 STEP 7: GET UNIQUE WORD COUNTS
# df_words = pd.read_csv('conf_matrix_words_count_by_class')
# print(df_words)
# unique_1 = (df_words['one'] != 0).sum()
# unique_2 = (df_words['two'] != 0).sum()
# unique_3 = (df_words['three'] != 0).sum()
# unique_4 = (df_words['four'] != 0).sum()
# unique_5 = (df_words['five'] != 0).sum()
# unique_6 = (df_words['six'] != 0).sum()
# unique_7 = (df_words['seven'] != 0).sum()
# unique_8 = (df_words['eight'] != 0).sum()
# unique_9 = (df_words['nine'] != 0).sum()
# unique_10 = (df_words['ten'] != 0).sum()
# unique_11 = (df_words['eleven'] != 0).sum()
# unique_12 = (df_words['twelve'] != 0).sum()
# unique_13 = (df_words['thirteen'] != 0).sum()
# unique_14 = (df_words['fourteen'] != 0).sum()
# unique_15 = (df_words['fifteen'] != 0).sum()
# unique_16 = (df_words['sixteen'] != 0).sum()
# unique_17 = (df_words['seventeen'] != 0).sum()
# unique_18 = (df_words['eighteen'] != 0).sum()
# unique_19 = (df_words['nineteen'] != 0).sum()
# unique_20 = (df_words['twenty'] != 0).sum()
# list_of_unique_word_count_by_class = [unique_1, unique_2, unique_3, unique_4, unique_5, unique_6, unique_7,
#                                       unique_8, unique_9, unique_10, unique_11, unique_12, unique_13, unique_14,
#                                       unique_15, unique_16, unique_17, unique_18, unique_19, unique_20]
# data = [list_of_unique_word_count_by_class]
# df_unique = pd.DataFrame(data, columns=None)
# df_unique.to_csv('conf_matrix_unique_word_counts_by_class', encoding='utf-8', index=False)


# QUESTION 4 STEP 6: GETTING MLE, CLASS PROBABILITIES P(Yk)
# df_class_probabilities = pd.read_csv('conf_matrix_training_set')
# list_of_probabilities = []
# total_all_entries = len(df_class_probabilities)
# for i in range(1, 21):
#     x = (df_class_probabilities['Class'].value_counts()[i])/total_all_entries
#     list_of_probabilities.append(x)
# thing = [list_of_probabilities]
# df_output = pd.DataFrame(list_of_probabilities, columns=['MLE P(Yk)'])
# print(df_output)
# df_output.to_csv('conf_matrix_MLE_P(Yk)_Probabilities', encoding='utf-8', index=False)


# QUESTION 4 STEP 5: COMBINE VOCAB WITH WORD COUNTS FROM conf_matrix_training.csv
# df_vocab = pd.read_csv("vocabulary.csv")
# df_counts = pd.read_csv("conf_matrix_all_word_counts")
# df_words_and_counts = pd.concat([df_vocab, df_counts], axis=1)
# print(df_words_and_counts)
# df_words_and_counts.to_csv('conf_matrix_words_count_by_class', encoding='utf-8', index=False)


# QUESTION 4 STEP 4: COMBINING ALL CLASS/WORD COUNTS FROM conf_matrix_training.csv INTO FINAL DATAFRAME,
# SAVING AS CSV FOR FAST REUSE
# df1 = pd.read_csv("CF_class_1")
# df2 = pd.read_csv("CF_class_2")
# df3 = pd.read_csv("CF_class_3")
# df4 = pd.read_csv("CF_class_4")
# df5 = pd.read_csv("CF_class_5")
# df6 = pd.read_csv("CF_class_6")
# df7 = pd.read_csv("CF_class_7")
# df8 = pd.read_csv("CF_class_8")
# df9 = pd.read_csv("CF_class_9")
# df10 = pd.read_csv("CF_class_10")
# df11 = pd.read_csv("CF_class_11")
# df12 = pd.read_csv("CF_class_12")
# df13 = pd.read_csv("CF_class_13")
# df14 = pd.read_csv("CF_class_14")
# df15 = pd.read_csv("CF_class_15")
# df16 = pd.read_csv("CF_class_16")
# df17 = pd.read_csv("CF_class_17")
# df18 = pd.read_csv("CF_class_18")
# df19 = pd.read_csv("CF_class_19")
# df20 = pd.read_csv("CF_class_20")
# df = pd.concat([df1, df2, df3, df4, df5, df6, df7, df8, df9, df10, df11, df12, df13, df14, df15, df16,
#                 df17, df18, df19, df20], axis=1)
# print(df)
# df.columns = ['one', 'two', 'three', 'four', 'five', 'six',
#               'seven', 'eight', 'nine', 'ten', 'eleven', 'twelve',
#               'thirteen', 'fourteen', 'fifteen', 'sixteen', 'seventeen',
#               'eighteen', 'nineteen', 'twenty']
# zeros = pd.DataFrame([0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]).T
# print(zeros)
# zeros.columns = ['one', 'two', 'three', 'four', 'five', 'six',
#                  'seven', 'eight', 'nine', 'ten', 'eleven', 'twelve',
#                  'thirteen', 'fourteen', 'fifteen', 'sixteen', 'seventeen',
#                  'eighteen', 'nineteen', 'twenty']
# conf_matrix_word_counts = pd.concat([zeros, df]).reset_index(drop=True)
# print(conf_matrix_word_counts)
# conf_matrix_word_counts.to_csv('conf_matrix_all_word_counts', encoding='utf-8', index=False)


# QUESTION 4 STEP 3: SAVING ALL COUNTS OF WORDS OF ALL CLASSES FROM conf_matrix_training.csv
# !!!!!!!!IMPORTANT!!!!!!!!
# this was run 20 times, one for each class, to create csv files
# df = pd.read_csv("conf_matrix_training_set")
# list_of_word_counts = []
# df_one = df.loc[df['Class'] == 20]
# df_one_trim = df_one.drop(['ID', 'Class'], axis=1)
# ls = df_one_trim['Word'].tolist()
# ls1 = []
# for i in ls:
#     if i not in ls1:
#         ls1.append(i)
# ls2 = df_one_trim.groupby(['Word'])['Count'].sum().tolist()
# list_final = []
# for i in range(len(ls2)):
#     x = ls1[i]
#     y = ls2[i]
#     temp = [x, y]
#     list_final.append(temp)
# df_one_final = pd.DataFrame(list_final, columns=['Word', 'Count'])
# df_final_sorted = df_one_final.sort_values(by='Word')
# master_list = []
# list_a = df_final_sorted['Word'].tolist()
# list_b = df_final_sorted['Count'].tolist()
# for i in range(1, 61189):
#     if len(list_a) > 0:
#         x = list_a[0]
#         y = list_b[0]
#         if x == i:
#             temp = [x, y]
#             master_list.append(temp)
#             list_a.pop(0)
#             list_b.pop(0)
#         else:
#             temp = [i, 0]
#             master_list.append(temp)
#     else:
#         temp = [i, 0]
#         master_list.append(temp)
# df_final_for_real = pd.DataFrame(master_list, columns=['Word', 'Count'])
# df_last = df_final_for_real.drop(['Word'], axis=1)
# print(df_last)
# df_last.to_csv('CF_class_20', encoding='utf-8', index=False)


# QUESTION 4 STEP 2: CREATE CSV FILES OF TRAINING AND TESTING SPLIT 80/20
# df_all = pd.read_csv('data_all_with_classes.csv')
# df_cf_test_set = pd.read_csv('conf_matrix_test_list.csv', names=['number'])
# test_list = df_cf_test_set['number'].tolist()
# train_list = []
# for i in range(12000):
#     if i not in test_list:
#         train_list.append(i)
# new_train = df_all[~df_all.ID.isin(test_list)]
# new_test = df_all[~df_all.ID.isin(train_list)]
# new_train.to_csv('conf_matrix_training_set', encoding='utf-8', index=False)
# new_test.to_csv('conf_matrix_testing_set', encoding='utf-8', index=False)


# QUESTION 4 STEP 1 SPLIT TRAINING TO TRAINING/TESTING 80/20 FOR CONFUSION MATRIX (9600 train, 2400 test)
# random_list = random.sample(range(0, 12000), 2400)
# random_list.sort()
# df = pd.DataFrame(random_list)
# df.to_csv('conf_matrix_test_list.csv', encoding='utf-8', index=False)


# QUESTION 7 STEP 3 COMPLETE WITH 100 WORDS
# df_idf = pd.read_csv('inverse_document_frequencies')
# idf_vector = df_idf['IDF'].tolist()
# idf_vector.pop(0)
# x1 = df_idf['one'].sum()
# x2 = df_idf['two'].sum()
# x3 = df_idf['three'].sum()
# x4 = df_idf['four'].sum()
# x5 = df_idf['five'].sum()
# x6 = df_idf['six'].sum()
# x7 = df_idf['seven'].sum()
# x8 = df_idf['eight'].sum()
# x9 = df_idf['nine'].sum()
# x10 = df_idf['ten'].sum()
# x11 = df_idf['eleven'].sum()
# x12 = df_idf['twelve'].sum()
# x13 = df_idf['thirteen'].sum()
# x14 = df_idf['fourteen'].sum()
# x15 = df_idf['fifteen'].sum()
# x16 = df_idf['sixteen'].sum()
# x17 = df_idf['seventeen'].sum()
# x18 = df_idf['eighteen'].sum()
# x19 = df_idf['nineteen'].sum()
# x20 = df_idf['twenty'].sum()
# total_counts = [x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13, x14, x15, x16, x17, x18, x19, x20]
# list_of_lists_of_words = []
# for i in range(20):
#     tf_idf_vector = []
#     tf_vector = []
#     for j in range(1, 61189):
#         x = df_idf.iat[j, i + 1]
#         y = x / total_counts[i]
#         tf_vector.append(y)
#     for i1, i2 in zip(tf_vector, idf_vector):
#         tf_idf_vector.append(i1 * i2)
#     list_of_hundred_words = []
#     for j in range(5):
#         z = tf_idf_vector.index(max(tf_idf_vector)) + 1
#         word = df_idf.iat[z, 0]
#         list_of_hundred_words.append(word)
#         tf_idf_vector[z - 1] = 0
#     list_of_lists_of_words.append(list_of_hundred_words)
# print(list_of_lists_of_words)
# df_out = pd.DataFrame(list_of_lists_of_words, columns=None)
# df_out.to_csv('top_100_words', encoding='utf-8', index=False)


# QUESTION 7 PART TWO: CALCULATE INVERSE DOCUMENT FREQUENCIES AND ADD TO words_count_by_class.csv
# df = pd.read_csv('words_count_by_class')
# list_of_inverse_document_frequencies = []
# for i in range(61189):
#     x = df.iloc[[i]].values.tolist()
#     x = x[0]
#     x.pop(0)
#     y = np.count_nonzero(x)
#     list_of_inverse_document_frequencies.append(math.log(20 / (y + 1)))
# df['IDF'] = list_of_inverse_document_frequencies
# df.to_csv('inverse_document_frequencies', encoding='utf-8', index=False)


# QUESTION #7 PART ONE: CALCULATE TERM FREQUENCY AND ADD TO data_all_with_classes.csv
# df = pd.read_csv('data_all_with_classes.csv')
# df_doc_split = df.groupby(['ID'])
# list_of_term_frequencies = []
# for i in range(12000):
#     current = df_doc_split.get_group(i)
#     total_word_count = current['Count'].sum()
#     for j in range(len(current)):
#         word_count = current.iat[j, 2]
#         tf = word_count/total_word_count
#         list_of_term_frequencies.append(tf)
# df['TF'] = list_of_term_frequencies
# df.to_csv('term_frequencies', encoding='utf-8', index=False)


# STEP 10: GETTING THE LIST OF UNIQUE WORD COUNTS BY CLASS PRINT TO CSV
# unique_1 = (df_words['one'] != 0).sum()
# unique_2 = (df_words['two'] != 0).sum()
# unique_3 = (df_words['three'] != 0).sum()
# unique_4 = (df_words['four'] != 0).sum()
# unique_5 = (df_words['five'] != 0).sum()
# unique_6 = (df_words['six'] != 0).sum()
# unique_7 = (df_words['seven'] != 0).sum()
# unique_8 = (df_words['eight'] != 0).sum()
# unique_9 = (df_words['nine'] != 0).sum()
# unique_10 = (df_words['ten'] != 0).sum()
# unique_11 = (df_words['eleven'] != 0).sum()
# unique_12 = (df_words['twelve'] != 0).sum()
# unique_13 = (df_words['thirteen'] != 0).sum()
# unique_14 = (df_words['fourteen'] != 0).sum()
# unique_15 = (df_words['fifteen'] != 0).sum()
# unique_16 = (df_words['sixteen'] != 0).sum()
# unique_17 = (df_words['seventeen'] != 0).sum()
# unique_18 = (df_words['eighteen'] != 0).sum()
# unique_19 = (df_words['nineteen'] != 0).sum()
# unique_20 = (df_words['twenty'] != 0).sum()
# list_of_unique_word_count_by_class = [unique_1, unique_2, unique_3, unique_4, unique_5, unique_6, unique_7,
#                                       unique_8, unique_9, unique_10, unique_11, unique_12, unique_13, unique_14,
#                                       unique_15, unique_16, unique_17, unique_18, unique_19, unique_20]
# data = [list_of_unique_word_count_by_class]
# df_unique = pd.DataFrame(data, columns=None)
# df_unique.to_csv('unique_count_by_class', encoding='utf-8', index=False)


# STEP 9: GETTING MLE, CLASS PROBABILITIES P(Yk)
# df_class_probabilities = pd.read_csv('data_all_with_classes.csv')
# list_of_probabilities = []
# total_all_entries = len(df_class_probabilities)
# for i in range(1, 21):
#     x = (df_class_probabilities['Class'].value_counts()[i])/total_all_entries
#     list_of_probabilities.append(x)
# thing = [list_of_probabilities]
# df_output = pd.DataFrame(list_of_probabilities, columns=['MLE P(Yk)'])
# print(df_output)
# df_output.to_csv('MLE_P(Yk)_Probabilities', encoding='utf-8', index=False)


# STEP 8: COMBINE VOCAB WITH WORD COUNTS FROM training.csv
# df_vocab = pd.read_csv("vocabulary.csv")
# df_counts = pd.read_csv("all_word_counts.csv")
# df_words_and_counts = pd.concat([df_vocab, df_counts], axis=1)
# print(df_words_and_counts)
# df_words_and_counts.to_csv('words_count_by_class', encoding='utf-8', index=False)


# STEP 7: COMBINING ALL CLASS/WORD COUNTS FROM training.csv INTO FINAL DATAFRAME, SAVING AS CSV FOR FAST REUSE
# df1 = pd.read_csv("class_1")
# df2 = pd.read_csv("class_2")
# df3 = pd.read_csv("class_3")
# df4 = pd.read_csv("class_4")
# df5 = pd.read_csv("class_5")
# df6 = pd.read_csv("class_6")
# df7 = pd.read_csv("class_7")
# df8 = pd.read_csv("class_8")
# df9 = pd.read_csv("class_9")
# df10 = pd.read_csv("class_10")
# df11 = pd.read_csv("class_11")
# df12 = pd.read_csv("class_12")
# df13 = pd.read_csv("class_13")
# df14 = pd.read_csv("class_14")
# df15 = pd.read_csv("class_15")
# df16 = pd.read_csv("class_16")
# df17 = pd.read_csv("class_17")
# df18 = pd.read_csv("class_18")
# df19 = pd.read_csv("class_19")
# df20 = pd.read_csv("class_20")
# df = pd.concat([df1, df2, df3, df4, df5, df6, df7, df8, df9, df10, df11, df12, df13, df14, df15, df16,
#                 df17, df18, df19, df20], axis=1)
# print(df)
# df.to_csv('all_word_counts', encoding='utf-8', index=False)


# STEP 6: SAVING ALL COUNTS OF WORDS OF ALL CLASSES FROM training.csv
# !!!!!!!!IMPORTANT!!!!!!!!
# this was run 20 times, one for each class, to create csv files
# df = pd.read_csv("data_all_with_classes.csv")
# list_of_word_counts = []
# df_one = df.loc[df['Class'] == 20]
# df_one_trim = df_one.drop(['ID', 'Class'], axis=1)
# ls = df_one_trim['Word'].tolist()
# ls1 = []
# for i in ls:
#     if i not in ls1:
#         ls1.append(i)
# ls2 = df_one_trim.groupby(['Word'])['Count'].sum().tolist()
# list_final = []
# for i in range(len(ls2)):
#     x = ls1[i]
#     y = ls2[i]
#     temp = [x, y]
#     list_final.append(temp)
# df_one_final = pd.DataFrame(list_final, columns=['Word', 'Count'])
# df_final_sorted = df_one_final.sort_values(by='Word')
# master_list = []
# list_a = df_final_sorted['Word'].tolist()
# list_b = df_final_sorted['Count'].tolist()
# for i in range(1, 61189):
#     if len(list_a) > 0:
#         x = list_a[0]
#         y = list_b[0]
#         if x == i:
#             temp = [x, y]
#             master_list.append(temp)
#             list_a.pop(0)
#             list_b.pop(0)
#         else:
#             temp = [i, 0]
#             master_list.append(temp)
#     else:
#         temp = [i, 0]
#         master_list.append(temp)
# df_final_for_real = pd.DataFrame(master_list, columns=['Word', 'Count'])
# df_last = df_final_for_real.drop(['Word'], axis=1)
# print(df_last)
# df_last.to_csv('class_20', encoding='utf-8', index=False)


# STEP 5: GETTING THE COUNTS OF CLASSES FROM training.csv
# df_in = pd.read_csv('data_all_with_classes.csv')
# df_mid = df_in.groupby(['ID']).min()
# df_out = df_mid.groupby(['Class']).sum()
# ls = df_out['Count'].tolist()
# print(df_out)
# print(ls)


# STEP 4: CREATING FILE data_all_testing.csv FOR TEST FILE
# df = pd.read_csv("sparse_matrix_testing.txt", sep='\t', header=None)
# df_first = df.loc[:, 0]
# df_second = df.loc[:, 1]
# first_list = []
# for i in range(len(df_first)):
#     x = df_first[i]
#     y = x.replace(' ', '').replace('(', '').replace(')', '')
#     z = y.split(',')
#     z.append(str(df_second[i]))
#     first_list.append(z)
# df_all = pd.DataFrame(first_list, columns=['ID', 'Word', 'Count'])
# df_all = df_all.loc[df_all['Word'] != '0']
# df_all['ID'] = df_all['ID'].astype('int')
# df_all['ID'] = df_all['ID'] + 12001
# print(df_all.head(10))
# df_all.to_csv('data_all_testing', encoding='utf-8', index=False)


# STEP 3: SETTING UP DATA CSV --> ID, WORD, COUNT, CLASS FROM training.csv
# df = pd.read_csv("sparse_matrix.txt", sep='\t', header=None)
# df_first = df.loc[:, 0]
# df_second = df.loc[:, 1]
# first_list = []
# for i in range(len(df_first)):
#     x = df_first[i]
#     y = x.replace(' ', '').replace('(', '').replace(')', '')
#     z = y.split(',')
#     z.append(str(df_second[i]))
#     first_list.append(z)
# df_all = pd.DataFrame(first_list, columns=['ID', 'Word', 'Count'])
# df_classes = df_all.loc[df_all['Word'] == '61189']
# list_of_classes = df_classes['Count'].tolist()
# df_all['Class'] = 0
# for i in range(len(df_all)):
#     x = df_all['ID'][i]
#     y = int(x)
#     df_all['Class'][i] = list_of_classes[y]
# df_all = df_all.loc[(df_all['Word'] != '61189') & (df_all['Word'] != '0')]
# print(df_all.head(10))
# df_all.to_csv('data_all_with_classes', encoding='utf-8', index=False)


# STEP 2: READING IN FILE: testing.csv PRINTING OUT SPARSE MATRIX
# start_time = time.time()
# df = pd.read_csv("testing.csv", header=None)
# x = scipy.sparse.csr_matrix(df.values)
# x.maxprint = x.count_nonzero()
# with open("sparse_matrix_testing.txt", "w") as file:
#     file.write(str(x))
#     file.close()
# end_time = time.time()
# print("time to execute: " + str(end_time - start_time) + "seconds.")


# STEP 1: READING IN THE FILE: training.csv PRINTING SPARSE MATRIX TO TEXT
# start_time = time.time()
# df = pd.read_csv("training.csv", header=None)
# x = scipy.sparse.csr_matrix(df.values)
# x.maxprint = x.count_nonzero()
# with open("sparse_matrix.txt", "w") as file:
#     file.write(str(x))
#     file.close()
# end_time = time.time()
# print("time to execute: " + str(end_time - start_time) + "seconds.")
