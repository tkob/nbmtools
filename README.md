# nbmtools

Nbmtools provides functionality for operating NetBeans module files.

## Usage

```
usage: nbmtools <subcommand> [<args>]

subcommands are:
    internalize
```

### `internalize`

```
nbmtools internalize <source URL or path> <output path>
```

`nbmtools internalize` copies source nbm files to destination, with all
 `.external` entries are internalized.

This effectively make the nbm file 'off-line', that is, you can intstall the
nbm file without accessing remote sites (probably Maven central repo).
