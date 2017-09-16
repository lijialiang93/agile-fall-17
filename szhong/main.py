# Name: Shaoliang Zhong, CWID: 10413243, Email: szhong2@stevens.edu

def read_ged(file):
    with open(file) as f:
        lines = f.readlines()
    lines = [x.strip() for x in lines]
    return lines


def decode_ged(lines):
    nodes = []
    for line in lines:
        parts = line.split()

        level = parts[0]
        parts.remove(level)

        tag = parts[0]
        parts.remove(tag)

        args = " ".join(parts)

        if args == "INDI" or args == "FAM":
            tmp = args
            args = tag
            tag = tmp

        node = {
            "level": level,
            "tag": tag,
            "args": args,
            "raw": line
        }

        nodes.append(node)
    return nodes


def validate_nodes(nodes):
    valid = ["INDI", "NAME", "SEX", "BIRT", "DEAT", "FAMC", "FAMS", "FAM", "MARR", "HUSB", "WIFE", "CHIL", "DIV", "DATE", "HEAD", "TRLR", "NOTE"]

    for node in nodes:
        is_valid = "Y"
        if (node["tag"] not in valid) or (node["tag"] == "DATE" and node["level"] == "1") or (node["tag"] == "NAME" and node["level"] == "2"):
            is_valid = "N"

        print("-->", node["raw"])
        print("<--", "%s|%s|%s|%s" % (node["level"], node["tag"], is_valid, node["args"]))


validate_nodes(decode_ged(read_ged("./A-Fake-Family.ged")))
