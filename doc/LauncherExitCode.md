
            case IOException ignored -> 1;
            case ClassNotFoundException ignored -> 2;
            case InvocationTargetException ignored -> 3;
            case NoSuchMethodException ignored -> 4;
            case InstantiationException ignored -> 5;
            case IllegalAccessException ignored -> 6;
            case null, default -> -1;