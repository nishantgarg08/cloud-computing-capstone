import os
import flask
from flask import request, jsonify

app = flask.Flask(__name__)
app.config["DEBUG"] = False
hot_partition = False
filepath = "/tmp/hotstatus"

@app.route('/consumer/notify/0', methods=['GET'])
def api_id():
    # Check if an ID was provided as part of the URL.
    # If ID is provided, assign it to a variable.
    # If no ID is provided, display an error in the browser.
    if 'hotStatus' in request.args:
        hot_partition = request.args['hotStatus']
    else:
        return "Error: No hotStatus field provided. Please specify an hotStatus."
    with open(filepath, 'w') as f:
        f.write(hot_partition)
    return 'Success'

@app.route('/consumer/partition/0', methods=['GET'])
def api_hotstatus():
    if os.path.isfile(filepath):
        with open(filepath, 'r') as f:
            return jsonify(f.read())
    else:
        hot_partition = False
    return jsonify(hot_partition)
app.run()

