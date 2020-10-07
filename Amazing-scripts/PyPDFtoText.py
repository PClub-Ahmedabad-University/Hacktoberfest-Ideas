'''
This is Python script that converts any PDF to text using tesseract-OCR. I made this to process pdfs in which text is not selectable.
Please donot use on normal pdfs of which you can just copy out text as this is a heavy to process and slowtask
author: Kaushal Patil
'''


import os
import fitz
import pytesseract
from PIL import Image as img


def main():
    """ main function """
    print("Enter the name of Pdf File (with Path if its not in Work Directory) You want to convert to text: example "+"newwork.pdf")
    filename = str(input())  # name of pdf file you want to render
    N = 0
    # render with PyMuPDF
    doc = fitz.open(filename)
    N = doc.pageCount
    print("Enter the name of text file (with path if its not in Work Directory) where you want to save")
    filetext = open(str(input())+".txt", "a")
    print("You May Enjoy a Coffee while the application converts all of pdf into text")
    for i in range(N):
        page = doc.loadPage(i)
        pix = page.getPixmap()
        pix.writePNG("out.png")
        # creates a temporary image to process text
        text = pytesseract.image_to_string(img.open('out.png'))
        filetext.write(text)
        print("completed {} out of {}".format(i + 1, N))
    os.remove("out.png")
    print("Task Completed")

    filetext.close()


if __name__ == "__main__":
    main()
