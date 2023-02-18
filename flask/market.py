"""Docstring"""
import pandas as pd
import numpy as np
import base64
import io

from flask import Flask, jsonify, request

app = Flask(__name__)

@app.route('/revan', methods= ['GET'])
def revan():
    return "revan pewka"

@app.route('/home', methods = ['GET', 'POST'])
def home_page():
    """docstring"""
    team = request.args.get('team')
    file_name = request.args.get('file_name')
    exam_id = request.args.get('exam_id')
    exam_id = int(exam_id)-1
    decoded_file = base64.b64decode(team)

    with open(file_name, 'wb') as f:  # write the decoded content to a file
        f.write(decoded_file)

    file = pd.read_excel(file_name, index_col=0, sheet_name=None)

    data = []
    for sheet_name in file.keys():
        df = file[sheet_name]
        df = df.applymap(lambda x: x.replace(',', '.') if isinstance(x, str) else x)
        
        df = df[df.index.isna() == False]
        
        df.columns = df.loc['S.No.']
        df.drop('S.No.', inplace=True)

        df = df.apply(pd.to_numeric, errors='coerce')
        df.index = df.iloc[:, 0]
        df = df[df.index.isna() == False]

        df = df.iloc[:, 1:]
        df = df.astype(float)

        json = df.iloc[:, exam_id].to_dict()

        for key, value in json.items():
            if np.isnan(value):
                json[key] = None
        data.append(json)




    sheet_names = file.keys()
    sgf = {
        'data': data,
        'sheetName': list(sheet_names)
    }
    print(sgf)
    return  jsonify(sgf)


if __name__ == "__main__":
    app.run(debug=True)
