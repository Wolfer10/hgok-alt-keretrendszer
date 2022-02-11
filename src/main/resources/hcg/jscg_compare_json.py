import copy
import json
import os
import sys


def create_schema():
    json_graph = {
        "directed": True,
        "multigraph": False,
        "nodes": list(),
        "links": list()
    }

    return copy.deepcopy(json_graph)


dir_tool_map = {
    # ez bele lett hackelve
    # os.path.join(sys.argv[1], 'js-callgraph-DEMAND'): 'acg-demand',
    # os.path.join(sys.argv[1], 'js-callgraph-ONESHOT'): 'acg-oneshot',
    # os.path.join(sys.argv[1], 'dyn-v8'): 'dyn-v8'
}

out_dir = os.path.join(sys.argv[1], 'x-compared')
data_container = {}


def compare(filter_entry=False, filter_wrapper=False):
    print("compare called")
    for dir in dir_tool_map.keys():
        print(dir)
        for file in os.listdir(dir):
            if file.endswith(".json"):
                data = json.load(open(os.path.join(dir, file)))
                print(data)
                if file not in data_container.keys():
                    data_container[file] = {}
                data_container[file][dir_tool_map[dir]] = data

    print(data_container.keys())
    for jsn in data_container.keys():
        edges = set()
        nodes = set()
        raw_nodes = {}
        raw_edges = {}
        node_map = {}
        for tool in data_container[jsn].keys():
            for node in data_container[jsn][tool]['nodes']:
                if node['pos'] in nodes:
                    if tool not in raw_nodes[node['pos']]['foundBy']:
                        raw_nodes[node['pos']]['foundBy'].append(tool)
                else:
                    nodes.add(node['pos'])
                    raw_nodes[node['pos']] = node
                    raw_nodes[node['pos']]['foundBy'] = [tool]
        for tool in data_container[jsn].keys():
            for link in data_container[jsn][tool]['links']:
                link_src = link['source']
                link_tgt = link['target']
                src_pos = None
                tgt_pos = None
                for node in data_container[jsn][tool]['nodes']:
                    if node['id'] == link_src:
                        src_pos = node['pos']
                    if node['id'] == link_tgt:
                        tgt_pos = node['pos']
                if src_pos is None or tgt_pos is None:
                    print('DEAD: ' + jsn + '(' + tool + ')' ', ' + str(link_src) + ' -> ' + str(link_tgt))
                    continue
                link_id = src_pos + "->" + tgt_pos
                if filter_wrapper and (tgt_pos.endswith(':1:0') or tgt_pos.endswith(':1:1')):
                    continue
                if filter_entry and src_pos == '<entry>':
                    continue
                if link_id in edges and tool not in raw_edges[link_id]['foundBy']:
                    raw_edges[link_id]['foundBy'].append(tool)
                else:
                    edges.add(link_id)
                    raw_edges[link_id] = link
                    raw_edges[link_id]['label'] = link_id
                    raw_edges[link_id]['foundBy'] = [tool]
        if not os.path.exists(out_dir):
            os.mkdir(out_dir)

        fp = open(os.path.join(out_dir, jsn), "w")
        j = create_schema()
        ctn = 0
        for k in raw_nodes.keys():
            raw_nodes[k]['id'] = ctn
            ctn += 1
            node_map[raw_nodes[k]['pos']] = raw_nodes[k]['id']
            j['nodes'].append(raw_nodes[k])
        for k in raw_edges.keys():
            ends = raw_edges[k]['label'].split('->')
            raw_edges[k]['source'] = node_map[ends[0]]
            raw_edges[k]['target'] = node_map[ends[1]]
            j['links'].append(raw_edges[k])
        print(j)
        json.dump(j, fp, indent=2)
        fp.close()


def change_dictionary_key_name(dict_object):
    final_obj = {}
    for key in dict_object.keys():
        final_obj[os.path.join(sys.argv[1], key)] = key 
    return final_obj


if __name__ == '__main__':
    with open(r"F:\Feri\egyetem\szakdoga\hgok-alt-keretrendszer\src\main\resources\static\working-dir\tool-names.json") as f:
        temp = json.loads(f.readlines()[0])
        dir_tool_map = change_dictionary_key_name(temp)

    #dir_tool_map = json.load(sys.argv[4])
    compare(filter_entry="noentry" in sys.argv, filter_wrapper="nowrapper" in sys.argv)

